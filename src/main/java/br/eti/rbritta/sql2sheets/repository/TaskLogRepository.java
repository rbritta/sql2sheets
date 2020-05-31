package br.eti.rbritta.sql2sheets.repository;

import br.eti.rbritta.sql2sheets.model.TaskLog;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskLogRepository extends BaseEntityRepository<TaskLog> {

    @Transactional
    Long deleteByTaskId(UUID taskId);

    List<TaskLog> findByTaskIdOrderByTimestampAsc(UUID taskId);
}
