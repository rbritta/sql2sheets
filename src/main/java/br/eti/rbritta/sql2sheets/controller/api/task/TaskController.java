package br.eti.rbritta.sql2sheets.controller.api.task;

import br.eti.rbritta.sql2sheets.model.Task;
import br.eti.rbritta.sql2sheets.service.AuditService;
import br.eti.rbritta.sql2sheets.service.TaskLogService;
import br.eti.rbritta.sql2sheets.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private AuditService audit;

    @Autowired
    private TaskService service;

    @Autowired
    private TaskLogService logService;

    @GetMapping
    public List<TaskResponse> getAll() {
        return service.getAllAs(TaskResponse::of);
    }

    @GetMapping("/{id}")
    public TaskResponse getOne(@PathVariable UUID id) {
        return service.getOneAs(id, TaskResponse::of);
    }

    @PutMapping("/{id}")
    public TaskResponse update(@PathVariable UUID id, @RequestBody TaskRequest request) {
        try {
            return TaskResponse.of(service.update(id, request));
        } finally {
            audit.log("PUT", "Task", id);
        }
    }

    @PostMapping("/{id}/clone")
    public TaskResponse clone(UUID id) {
        try {
            return TaskResponse.of(service.clone(id));
        } finally {
            audit.log("CLONE", "Task", id);
        }
    }

    @PostMapping
    public TaskResponse create(@RequestBody TaskRequest request) {
        final Task task = service.create(request);
        try {
            return TaskResponse.of(task);
        } finally {
            audit.log("POST", "Task", task.getId());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
        audit.log("DELETE", "Task", id);
    }

    @PostMapping("/{id}/execute")
    public void execute(@PathVariable UUID id) {
        service.asyncExecute(id);
        audit.log("EXECUTE", "Task", id);
    }

    @GetMapping("/{id}/logs")
    public LogResponse getLogs(@PathVariable UUID id) {
        return LogResponse.of(logService.list(id));
    }

    @DeleteMapping("/{id}/logs")
    public void clearLog(@PathVariable UUID id) {
        logService.deleteAll(id);
        audit.log("DELETE", "Logs of Task", id);
    }
}