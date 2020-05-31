package br.eti.rbritta.sql2sheets.service;

import br.eti.rbritta.sql2sheets.controller.api.datasource.DataSourceRequest;
import br.eti.rbritta.sql2sheets.exception.DataSourceNotFoundException;
import br.eti.rbritta.sql2sheets.model.DataSourceConfig;
import br.eti.rbritta.sql2sheets.model.DataSourceStatus;
import br.eti.rbritta.sql2sheets.repository.DataSourceConfigRepository;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.UUID;

import static java.util.Objects.isNull;

@Service
public class DataSourceService extends BaseEntityService<DataSourceConfig, DataSourceConfigRepository> {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceService.class);

    @Autowired
    private DataSourceConfigRepository repo;

    @Override
    public DataSourceConfigRepository repo() {
        return repo;
    }

    public BasicDataSource createDataSource(DataSourceConfig config) {
        final BasicDataSource ds = new BasicDataSource();
        ds.setUrl(config.getUrl());
        ds.setUsername(config.getUsername());
        ds.setPassword(config.getPassword());

        ds.setLogAbandoned(true);
        ds.setAbandonedUsageTracking(true);
        ds.setLogExpiredConnections(true);

        ds.setInitialSize(0);
        ds.setMinIdle(0);
        ds.setMaxIdle(3);
        ds.setMaxTotal(8);
        ds.setMaxWaitMillis(180000);

        ds.setPoolPreparedStatements(true);

        return ds;
    }

    public DataSourceConfig test(UUID configId) {
        try (final BasicDataSource ds = createDataSource(getOneConfig(configId))) {
            final DataSourceStatus status = DataSourceStatus.of(ds.getConnection().isValid(1000));
            return updateStatus(configId, status);
        } catch (SQLException e) {
            logger.error("Error while testing connection of " + configId, e);
            return updateStatus(configId, DataSourceStatus.INVALID);
        }
    }

    public DataSourceConfig updateStatus(UUID configId, DataSourceStatus status) {
        final DataSourceConfig config = getOneConfig(configId);
        config.setStatus(status);
        return repo().save(config);
    }

    public DataSourceConfig getOneConfigByName(String name) {
        final DataSourceConfig ds = repo().findByName(name);
        if (isNull(ds)) {
            throw new DataSourceNotFoundException();
        }
        return ds;
    }

    public DataSourceConfig createConfig(DataSourceRequest request) {
        DataSourceConfig ds = new DataSourceConfig();
        ds.setUsername(request.getUsername());
        ds.setName(request.getName());
        ds.setUrl(request.getUrl());
        ds.setPassword(request.getPassword());
        ds.setStatus(DataSourceStatus.UNDEFINED);

        repo().save(ds);

        return ds;
    }

    public DataSourceConfig getOneConfig(UUID id) {
        return repo().findById(id).orElseThrow(DataSourceNotFoundException::new);
    }

    public DataSourceConfig updateConfig(UUID id, DataSourceRequest request) {
        final DataSourceConfig ds = getOneConfig(id);
        ds.setUsername(request.getUsername());
        ds.setName(request.getName());
        ds.setUrl(request.getUrl());
        ds.setPassword(request.getPassword());
        ds.setStatus(DataSourceStatus.UNDEFINED);

        repo().save(ds);

        return ds;
    }
}
