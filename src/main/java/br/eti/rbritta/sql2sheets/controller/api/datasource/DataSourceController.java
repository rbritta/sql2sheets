package br.eti.rbritta.sql2sheets.controller.api.datasource;

import br.eti.rbritta.sql2sheets.controller.web.NavigationHandler;
import br.eti.rbritta.sql2sheets.model.DataSourceConfig;
import br.eti.rbritta.sql2sheets.service.AuditService;
import br.eti.rbritta.sql2sheets.service.DataSourceService;
import br.eti.rbritta.sql2sheets.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/datasources")
public class DataSourceController {

    @Autowired
    private AuditService audit;

    @Autowired
    private DataSourceService service;
 
    @GetMapping
    public List<DataSourceResponse> getAll() {
        return service.getAllAs(DataSourceResponse::of);
    }

    @GetMapping("/{id}")
    public DataSourceResponse getOne(@PathVariable UUID id) {
        return service.getOneAs(id, DataSourceResponse::of);
    }

    @PutMapping("/{id}")
    public DataSourceResponse update(@PathVariable UUID id, @RequestBody DataSourceRequest request) {
        try {
            return DataSourceResponse.of(service.updateConfig(id, request));
        } finally {
            audit.log("PUT", "DataSource", id);
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
        audit.log("DELETE", "DataSource", id);
    }

    @GetMapping("/{id}/test")
    public DataSourceResponse testConnection(@PathVariable UUID id) {
        try {
            return DataSourceResponse.of(service.test(id));
        } finally {
            audit.log("TEST", "DataSource", id);
        }
    }

    @PostMapping
    public DataSourceResponse create(@RequestBody DataSourceRequest request) {
        final DataSourceConfig config = service.createConfig(request);
        try {
            return DataSourceResponse.of(config);
        } finally {
            audit.log("POST", "DataSource", config.getId());
        }
    }
}