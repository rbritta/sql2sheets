package br.eti.rbritta.sql2sheets.controller.web;

import br.eti.rbritta.sql2sheets.controller.api.datasource.DataSourceController;
import br.eti.rbritta.sql2sheets.controller.api.task.TaskController;
import br.eti.rbritta.sql2sheets.controller.api.task.TaskRequest;
import br.eti.rbritta.sql2sheets.controller.api.task.TaskResponse;
import br.eti.rbritta.sql2sheets.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/tasks")
public class ViewTaskController {

    @Autowired
    private DataSourceController dataSourcesApi;

    @Autowired
    private TaskController taskApi;

    @Autowired
    private NavigationHandler nav;

    @GetMapping
    public String openNewTask(Model model) {
        return nav.renderTask(model, null, TaskResponse.NEW, dataSourcesApi.getAll());
    }

    @GetMapping("/{id}")
    public String openExistingTask(Model model, @PathVariable UUID id) {
        return nav.renderTask(model, null, taskApi.getOne(id), dataSourcesApi.getAll());
    }

    @PostMapping("/create")
    public String createTask(Model model, @ModelAttribute TaskRequest request) {
        try {
            taskApi.create(request);
            return nav.redirectRoot();
        } catch (Exception e) {
            return nav.renderTask(model, e.getMessage(), TaskResponse.of(null, request), dataSourcesApi.getAll());
        }
    }

    @PostMapping("/{id}/update")
    public String updateTask(Model model, @PathVariable UUID id, @ModelAttribute TaskRequest request) {
        try {
            taskApi.update(id, request);
            return nav.redirectRoot();
        } catch (Exception e) {
            return nav.renderTask(model, e.getMessage(), TaskResponse.of(id, request), dataSourcesApi.getAll());
        }
    }

    @GetMapping("/{id}/clone")
    public String cloneTask(Model model, @PathVariable UUID id) {
        try {
            final TaskResponse newTask = taskApi.clone(id);
            return openExistingTask(model, newTask.getId());
        } catch (Exception e) {
            return nav.renderTask(model, e.getMessage(), taskApi.getOne(id), dataSourcesApi.getAll());
        }
    }

    @GetMapping("/{id}/execute")
    public String executeTask(@PathVariable UUID id) {
        taskApi.execute(id);
        return nav.redirectRoot();
    }

    @GetMapping("/{id}/delete")
    public String deleteTask(Model model, @PathVariable UUID id) {
        try {
            taskApi.delete(id);
            return nav.redirectRoot();
        } catch (Exception e) {
            return nav.renderTask(model, e.getMessage(), taskApi.getOne(id), dataSourcesApi.getAll());
        }
    }

}
