package br.eti.rbritta.sql2sheets.service;

import br.eti.rbritta.sql2sheets.controller.api.task.TaskResponse;
import br.eti.rbritta.sql2sheets.model.DataSourceStatus;
import br.eti.rbritta.sql2sheets.model.Task;
import br.eti.rbritta.sql2sheets.model.TaskSheet;
import br.eti.rbritta.sql2sheets.model.TaskStatus;
import br.eti.rbritta.sql2sheets.repository.TaskRepository;
import com.google.api.services.sheets.v4.Sheets;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static br.eti.rbritta.sql2sheets.utils.TimeUtils.DDMMYYYY_HHMMSS;
import static br.eti.rbritta.sql2sheets.utils.TimeUtils.format;
import static com.mchange.v2.lang.StringUtils.nonEmptyString;
import static java.time.LocalDateTime.now;
import static java.util.Objects.isNull;

@Service
public class TaskExecutorService {

    private static final Logger logger = LoggerFactory.getLogger(TaskExecutorService.class);

    @Autowired
    private DataSourceService dsService;

    @Autowired
    private SpreadsheetService spreadsheetService;

    @Autowired
    private TaskLogService logService;

    @Autowired
    private TaskRepository taskRepo;

    public void execute(Task task, BasicDataSource ds) {
        if (taskRepo.isRunning(task.getId())) {
            logger.info("Cannot execute Task: {}. Already running.", task.getName());
            logService.log(task, "Cannot execute. Already Running");
            return;
        }

        logger.info("Executing: {}", task.getName());
        logService.log(task, "Executing...");

        task.setStatus(TaskStatus.RUNNING);
        taskRepo.save(task);

        final UUID dsConfigId = task.getDataSourceConfig().getId();
        final String taskName = task.getName();
        final TaskSheet taskSheet = task.getSheet();
        final String sheetId = taskSheet.getId();;
        final String rangeData = taskSheet.getRangeData();

        try {
            List<Map<String, Object>> data = new QueryRunner(ds).query(task.getQuery(), new MapListHandler());
            logger.info("{} - Dada loaded from {}", taskName, task.getDataSourceConfig().getName());
            logService.log(task, "Data loaded: " + data.size() + " rows");

            final Sheets sheets = spreadsheetService.getSheets(taskSheet);

            spreadsheetService.fill(sheets, sheetId, rangeData, data);
            logger.info("{} - Data updated on Spreadsheet {} {}", taskName, sheetId, rangeData);
            logService.log(task, "Data updated on " + sheetId + " / " + rangeData);

            final String timestampCell = taskSheet.getRangeTimestamp();
            if (nonEmptyString(timestampCell)) {
                spreadsheetService.fill(sheets, sheetId, timestampCell, format(now(), DDMMYYYY_HHMMSS));
                logger.info("{} - Timestamp updated on Spreadsheet {} {}", taskName, sheetId, timestampCell);
                logService.log(task, "Timestamp updated on " + sheetId + " / " + timestampCell);
            }

            logger.info("Executed: {}", task.getName());
            logService.log(task, "Executed");
            dsService.updateStatus(dsConfigId, DataSourceStatus.VALID);
            task.setStatus(TaskStatus.VALID);

        } catch (SQLException e) {
            final String dsInfo = isNull(ds) ? "undefined DataSource" : ds.getUrl() + " / " + ds.getUsername();
            logger.error(taskName + " - Error while querying data on " + dsInfo, e);
            logService.log(task, "Error while querying data on " + dsInfo + ". " + e.getLocalizedMessage());
            dsService.updateStatus(dsConfigId, DataSourceStatus.INVALID);
            task.setStatus(TaskStatus.INVALID);

        } catch (GeneralSecurityException | IOException e) {
            logger.error(taskName + " - Error while updating data on " + sheetId, e);
            logService.log(task, "Error while updating data on " + sheetId + " / " + rangeData + ". " + e.getLocalizedMessage());
            task.setStatus(TaskStatus.INVALID);

        } catch (Exception e) {
            logger.error(taskName + " - Undefined error", e);
            logService.log(task, "Undefined error: " + e.getLocalizedMessage());
            dsService.updateStatus(dsConfigId, DataSourceStatus.UNDEFINED);
            task.setStatus(TaskStatus.INVALID);

        } finally {
            task.setLastExecution(now());
            taskRepo.save(task);
        }
    }
}
