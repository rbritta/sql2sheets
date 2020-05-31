package br.eti.rbritta.sql2sheets.controller.api.driver;

import br.eti.rbritta.sql2sheets.controller.api.datasource.DataSourceResponse;
import br.eti.rbritta.sql2sheets.service.AuditService;
import br.eti.rbritta.sql2sheets.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    @Autowired
    private AuditService audit;

    @Autowired
    private DriverService service;

    @GetMapping
    public List<DriverResponse> getAll() {
        return service.getAllAs(DriverResponse::of);
    }
 
    @PostMapping
    public DriverResponse create(MultipartFile file) throws IOException {
        final File driver = service.add(file.getOriginalFilename(), file.getBytes());
        try {
            return DriverResponse.of(driver);
        } finally {
            audit.log("POST", "Driver", driver.getName());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
        audit.log("DELETE", "Driver", id);
    }
}