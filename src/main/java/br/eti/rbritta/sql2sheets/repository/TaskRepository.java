package br.eti.rbritta.sql2sheets.repository;

import br.eti.rbritta.sql2sheets.model.Task;
import br.eti.rbritta.sql2sheets.model.TaskStatus;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static java.util.Objects.nonNull;

@Repository
public interface TaskRepository extends BaseEntityRepository<Task> {

    default boolean isRunning(UUID id) {
//        return nonNull(findByIdAndStatus(id, TaskStatus.RUNNING));
        return existsByIdAndStatus(id, TaskStatus.RUNNING);
    }

//    Task findByIdAndStatus(UUID id, TaskStatus status);

    boolean existsByIdAndStatus(UUID id, TaskStatus status);
}
