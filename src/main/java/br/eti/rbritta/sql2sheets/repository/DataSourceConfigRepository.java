package br.eti.rbritta.sql2sheets.repository;

import br.eti.rbritta.sql2sheets.model.DataSourceConfig;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSourceConfigRepository extends BaseEntityRepository<DataSourceConfig> {

    DataSourceConfig findByName(String name);

}
