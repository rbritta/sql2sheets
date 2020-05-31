package br.eti.rbritta.sql2sheets.controller.api.task;

import br.eti.rbritta.sql2sheets.model.DataSourceConfig;
import br.eti.rbritta.sql2sheets.model.Task;
import br.eti.rbritta.sql2sheets.model.TaskSheet;
import br.eti.rbritta.sql2sheets.model.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

    public static final TaskResponse NEW = new TaskResponse();

    private UUID id;
    private String name;
    private String owner;
    private String cron;
    private String query;
    private String dataSourceName;
    private String sheetId;
    private String sheetDataRange;
    private String sheetTimeCell;
    private String sheetAuthorization;
    private boolean active;
    private LocalDateTime lastExecution;
    private LocalDateTime nextExecution;
    private TaskStatus status;

    public static TaskResponse of(Task entity) {

        final DataSourceConfig dsConfig = entity.getDataSourceConfig();
        final TaskSheet sheet = entity.getSheet();

        return new TaskResponse(
                entity.getId(),
                entity.getName(),
                entity.getOwner(),
                entity.getCron(),
                entity.getQuery(),
                isNull(dsConfig) ? null : dsConfig.getName(),
                isNull(sheet) ? null : sheet.getId(),
                isNull(sheet) ? null : sheet.getRangeData(),
                isNull(sheet) ? null : sheet.getRangeTimestamp(),
                isNull(sheet) ? null : sheet.getAuthorization(),
                entity.isActive(),
                entity.getLastExecution(),
                entity.getNextExecution(),
                entity.getStatus());
    }

    public static TaskResponse of(UUID id, TaskRequest request) {
        return new TaskResponse(
                id,
                request.getName(),
                request.getOwner(),
                request.getCron(),
                request.getQuery(),
                request.getDataSourceName(),
                request.getSheetId(),
                request.getSheetDataRange(),
                request.getSheetTimeCell(),
                request.getSheetAuthorization(),
                request.isActive(),
                null,
                null,
                TaskStatus.UNDEFINED);
    }

    public TaskStatus getStatus() {
        return nonNull(status) ? status : TaskStatus.UNDEFINED;
    }
}
