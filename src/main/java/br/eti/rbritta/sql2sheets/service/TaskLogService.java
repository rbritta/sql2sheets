package br.eti.rbritta.sql2sheets.service;

import br.eti.rbritta.sql2sheets.model.Task;
import br.eti.rbritta.sql2sheets.model.TaskLog;
import br.eti.rbritta.sql2sheets.repository.TaskLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TaskLogService {

    @Autowired
    private TaskLogRepository repo;

    public List<TaskLog> list(UUID taskId) {
        return repo.findByTaskIdOrderByTimestampAsc(taskId);
    }

    public void log(Task task, String message) {
        repo.save(TaskLog.of(task, LocalDateTime.now(), message));
    }

    public void deleteAll(UUID taskId) {
        repo.deleteByTaskId(taskId);
    }
}
