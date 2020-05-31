package br.eti.rbritta.sql2sheets.controller.web;

import br.eti.rbritta.sql2sheets.controller.api.task.TaskController;
import br.eti.rbritta.sql2sheets.controller.api.task.LogResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/tasks/{id}/logs")
public class ViewLogController {

    @Autowired
    private TaskController taskApi;

    @Autowired
    private NavigationHandler nav;

    @GetMapping
    public String taskLogs(Model model, @PathVariable UUID id) {
        return nav.renderLogs(model, taskApi.getOne(id), taskApi.getLogs(id));
    }

    @GetMapping("/data-fragment")
    public String logsFragment(Model model, @PathVariable UUID id) {
        try {
            return nav.renderLogsLines(model, taskApi.getLogs(id));
        } catch (Exception e) {
            return nav.renderLogsLines(model, LogResponse.of(e.getMessage()));
        }
    }

    @GetMapping("/clear")
    public String clearLogs(Model model, @PathVariable UUID id) {
        taskApi.clearLog(id);
        return nav.renderLogs(model, taskApi.getOne(id), taskApi.getLogs(id));
    }
}
