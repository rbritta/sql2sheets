package br.eti.rbritta.sql2sheets.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.UUID;

import static br.eti.rbritta.sql2sheets.utils.TimeUtils.DDMMYYYY_HHMMSS;
import static br.eti.rbritta.sql2sheets.utils.TimeUtils.format;
import static org.thymeleaf.util.StringUtils.abbreviate;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TaskLog extends BaseEntity {

    private static final int MESSAGE_MAX_SIZE = 4000;

    @NotNull
    private UUID taskId;

    @NotNull
    private LocalDateTime timestamp;

    @NotNull
    @Column(length = MESSAGE_MAX_SIZE)
    private String message;

    public static TaskLog of(Task task, LocalDateTime date, String message) {
        message = abbreviate(message, MESSAGE_MAX_SIZE);
        return new TaskLog(task.getId(), date, message);
    }

    public String asLine() {
        return format(timestamp, DDMMYYYY_HHMMSS) + " - " + message;
    }
}
