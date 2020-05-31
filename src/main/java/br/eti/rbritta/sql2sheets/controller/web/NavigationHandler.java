package br.eti.rbritta.sql2sheets.controller.web;

import br.eti.rbritta.sql2sheets.config.AuthenticationProperties;
import br.eti.rbritta.sql2sheets.controller.api.datasource.DataSourceResponse;
import br.eti.rbritta.sql2sheets.controller.api.driver.DriverResponse;
import br.eti.rbritta.sql2sheets.controller.api.task.LogResponse;
import br.eti.rbritta.sql2sheets.controller.api.task.TaskResponse;
import br.eti.rbritta.sql2sheets.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;

@Component
public class NavigationHandler {

    private static final Logger logger = LoggerFactory.getLogger(NavigationHandler.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationProperties authProperties;

    @Value("${spring.application.name}")
    private String appName;

    public String redirectRoot() {
        return "redirect:/";
    }

    public String renderHome(Model model, List<TaskResponse> tasks, List<DataSourceResponse> dataSources) {
        addAppAttributes(model);
        model.addAttribute("tasks", tasks);
        model.addAttribute("dataSources", dataSources);
        return audit("home");
    }

    public String renderDataSource(Model model, String message, DataSourceResponse dataSource) {
        addAppAttributes(model);
        addMessage(model, message);
        model.addAttribute("dataSource", dataSource);
        return audit("datasource", dataSource.getId());
    }

    public String renderTask(Model model, String message, TaskResponse task, List<DataSourceResponse> dataSources) {
        addAppAttributes(model);
        addMessage(model, message);
        model.addAttribute("task", task);
        model.addAttribute("datasources", dataSources);
        return audit("task", task.getId());
    }

    public String renderLogs(Model model, TaskResponse task, LogResponse log) {
        addAppAttributes(model);
        model.addAttribute("task", task);
        model.addAttribute("logs", log);
        return audit("logs", task.getId());
    }

    public String renderLogsLines(Model model, LogResponse log) {
        model.addAttribute("logs", log);
        return "logs :: #log-data";
    }

    public String renderDriver(Model model, List<DriverResponse> drivers) {
        addAppAttributes(model);
        model.addAttribute("drivers", drivers);
        return audit("driver");
    }

    public String redirectDrivers(RedirectAttributes redirect, String message, List<DriverResponse> response) {
        addAppAttributes(redirect);
        addMessage(redirect, message);
        redirect.addFlashAttribute("drivers", response);
        return "redirect:/drivers";
    }

    private void addAppAttributes(Model model) {
        if (model instanceof RedirectAttributes) {
            ((RedirectAttributes) model).addFlashAttribute("appName", appName);
            ((RedirectAttributes) model).addFlashAttribute("authType", authProperties.getAuthType());
        } else {
            model.addAttribute("appName", appName);
            model.addAttribute("authType", authProperties.getAuthType());
        }
    }

    private void addMessage(Model model, String message) {
        if (isNull(message)) {
            return;
        }
        if (model instanceof RedirectAttributes) {
            ((RedirectAttributes) model).addFlashAttribute("message", message);
        } else {
            model.addAttribute("message", message);
        }
    }

    private String audit(String resource) {
        return audit(resource, null);
    }

    private String audit(String resource, UUID id) {
        logger.info("{} navigated to " + resource + (isNull(id) ? "" : "[{}]"), userService.getCurrentUsername(), id);
        return resource;
    }
}
