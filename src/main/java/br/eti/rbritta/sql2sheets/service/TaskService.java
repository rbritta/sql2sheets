package br.eti.rbritta.sql2sheets.service;

import br.eti.rbritta.sql2sheets.config.SpreadsheetProperties;
import br.eti.rbritta.sql2sheets.controller.api.task.TaskRequest;
import br.eti.rbritta.sql2sheets.exception.TaskNotFoundException;
import br.eti.rbritta.sql2sheets.model.Task;
import br.eti.rbritta.sql2sheets.model.TaskSheet;
import br.eti.rbritta.sql2sheets.model.TaskStatus;
import br.eti.rbritta.sql2sheets.repository.TaskRepository;
import br.eti.rbritta.sql2sheets.scheduler.TaskScheduler;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static br.eti.rbritta.sql2sheets.ApplicationConfig.TASK_ASYNC_EXECUTOR;
import static java.util.Objects.nonNull;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Service
public class TaskService extends BaseEntityService<Task, TaskRepository> {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private DataSourceService dsService;

    @Autowired
    private SpreadsheetProperties spreadsheetProperties;

    @Autowired
    private TaskExecutorService executor;

    @Autowired
    private TaskLogService logService;

    @Autowired
    private TaskRepository repo;

    @Autowired
    private TaskScheduler scheduler;

    @Autowired
    private UserService userService;

    @Override
    public TaskRepository repo() {
        return repo;
    }

    public Task getOne(UUID id) {
        return complete(repo().findById(id).orElseThrow(TaskNotFoundException::new));
    }

    public <N> List<N> getAllAs(Function<Task, N> function) {
        return super.getAllAs(task -> function.apply(this.complete(task)));
    }

    public Task complete(Task task) {
        task.setNextExecution(scheduler.getNextExecutionTime(task));
        TaskSheet sheet = task.getSheet();
        if (nonNull(sheet)) {
            sheet.setUrl(spreadsheetProperties.getUrl(sheet.getId()));
        }
        return task;
    }

    public Task update(UUID id, TaskRequest request) {
        final Task task = fill(getOne(id), request);
        repo.save(task);
        scheduler.schedule(task);
        return task;
    }

    @Async(TASK_ASYNC_EXECUTOR)
    public void asyncExecute(UUID id) {
        final Task task = getOne(id);
        logger.info("Execution triggered by User");
        logService.log(task, "Execution triggered by User");
        final BasicDataSource ds = dsService.createDataSource(task.getDataSourceConfig());
        executor.execute(task, ds);
    }

    public Task clone(UUID id) {
        final TaskRequest request = TaskRequest.of(getOne(id));
        request.setName(request.getName() + " (Clone)");
        request.setOwner(userService.getCurrentUsername());
        return create(request);
    }

    public Task create(TaskRequest request) {
        final Task task = fill(new Task(), request);
        repo.save(task);
        scheduler.schedule(task);
        return task;
    }

    private Task fill(Task task, TaskRequest request) {
        task.setName(request.getName());
        task.setOwner(request.getOwner());
        task.setCron(request.getCron());
        task.setQuery(request.getQuery());
        task.setDataSourceConfig(dsService.getOneConfigByName(request.getDataSourceName()));
        task.setStatus(TaskStatus.UNDEFINED);
        task.setActive(request.isActive());

        final TaskSheet sheet = new TaskSheet();
        sheet.setId(request.getSheetId());
        sheet.setRangeData(request.getSheetDataRange());
        sheet.setRangeTimestamp(request.getSheetTimeCell());
        sheet.setAuthorization(request.getSheetAuthorization());
        task.setSheet(sheet);

        return task;
    }
}
