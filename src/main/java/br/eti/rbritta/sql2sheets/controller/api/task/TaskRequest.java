package br.eti.rbritta.sql2sheets.controller.api.task;

import br.eti.rbritta.sql2sheets.model.DataSourceConfig;
import br.eti.rbritta.sql2sheets.model.Task;
import br.eti.rbritta.sql2sheets.model.TaskSheet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static java.util.Objects.isNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {

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

    public static TaskRequest of(Task task) {
        final DataSourceConfig ds = task.getDataSourceConfig();
        final TaskSheet sheet = task.getSheet();
        return new TaskRequest(
                task.getName(),
                task.getOwner(),
                task.getCron(),
                task.getQuery(),
                isNull(ds) ? null : ds.getName(),
                isNull(sheet) ? null : sheet.getId(),
                isNull(sheet) ? null : sheet.getRangeData(),
                isNull(sheet) ? null : sheet.getRangeTimestamp(),
                isNull(sheet) ? null : sheet.getAuthorization(),
                task.isActive()
        );
    }
}
