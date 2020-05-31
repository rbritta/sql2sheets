package br.eti.rbritta.sql2sheets.controller.web;

import br.eti.rbritta.sql2sheets.controller.api.datasource.DataSourceController;
import br.eti.rbritta.sql2sheets.controller.api.task.TaskController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewHomeController {

    @Autowired
    private TaskController taskApi;

    @Autowired
    private DataSourceController dataSourcesApi;

    @Autowired
    private NavigationHandler nav;

    @GetMapping("/")
    public String openHome(Model model) {
        return nav.renderHome(model, taskApi.getAll(), dataSourcesApi.getAll());
    }
}
