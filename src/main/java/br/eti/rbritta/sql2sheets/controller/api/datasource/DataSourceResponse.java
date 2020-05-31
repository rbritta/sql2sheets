package br.eti.rbritta.sql2sheets.controller.api.datasource;

import br.eti.rbritta.sql2sheets.model.DataSourceConfig;
import br.eti.rbritta.sql2sheets.model.DataSourceStatus;
import br.eti.rbritta.sql2sheets.model.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static java.util.Objects.nonNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DataSourceResponse {

    public static final DataSourceResponse NEW = new DataSourceResponse();

    private UUID id;
    private String name;
    private String url;
    private String username;
    private String password;
    private DataSourceStatus status;

    public static DataSourceResponse of(DataSourceConfig ds) {
        return new DataSourceResponse(
                ds.getId(),
                ds.getName(),
                ds.getUrl(),
                ds.getUsername(),
                ds.getPassword(),
                ds.getStatus());
    }

    public static DataSourceResponse of(UUID id, DataSourceRequest request) {
        return new DataSourceResponse(
                id,
                request.getName(),
                request.getUrl(),
                request.getUsername(),
                request.getPassword(),
                DataSourceStatus.UNDEFINED);
    }

    public DataSourceStatus getStatus() {
        return nonNull(status) ? status : DataSourceStatus.UNDEFINED;
    }
}
