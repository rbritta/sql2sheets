package br.eti.rbritta.sql2sheets.controller.web;

import br.eti.rbritta.sql2sheets.controller.api.datasource.DataSourceRequest;
import br.eti.rbritta.sql2sheets.controller.api.datasource.DataSourceController;
import br.eti.rbritta.sql2sheets.controller.api.datasource.DataSourceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/datasources")
public class ViewDataSourceController {

    @Autowired
    private DataSourceController dataSourcesApi;

    @Autowired
    private NavigationHandler nav;

    @GetMapping
    public String openBlankDataSource(Model model) {
        return nav.renderDataSource(model, null, DataSourceResponse.NEW);
    }

    @GetMapping("/{id}")
    public String openExistingDataSource(Model model, @PathVariable UUID id) {
        return nav.renderDataSource(model, null, dataSourcesApi.getOne(id));
    }

    @PostMapping("/create")
    public String createDataSource(Model model, @ModelAttribute DataSourceRequest request) {
        try {
            dataSourcesApi.create(request);
            return nav.redirectRoot();
        } catch (Exception e) {
            return nav.renderDataSource(model, e.getMessage(), DataSourceResponse.of(null, request));
        }
    }

    @PostMapping("/{id}/update")
    public String updateDataSource(Model model, @ModelAttribute DataSourceRequest request, @PathVariable UUID id) {
        try {
            dataSourcesApi.update(id, request);
            return nav.redirectRoot();
        } catch (Exception e) {
            return nav.renderDataSource(model, e.getMessage(), DataSourceResponse.of(id, request));
        }
    }

    @GetMapping("/{id}/test")
    public String testDataSource(@PathVariable UUID id) {
        dataSourcesApi.testConnection(id);
        return nav.redirectRoot();
    }

    @GetMapping("/{id}/delete")
    public String deleteDataSource(Model model, @PathVariable UUID id) {
        try {
            dataSourcesApi.delete(id);
            return nav.redirectRoot();
        } catch (Exception e) {
            return nav.renderDataSource(model, e.getMessage(), dataSourcesApi.getOne(id));
        }
    }

}
