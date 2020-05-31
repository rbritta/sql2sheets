package br.eti.rbritta.sql2sheets.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

import static br.eti.rbritta.sql2sheets.model.TaskStatus.RUNNING;

@Setter
@Getter
@Entity
public class Task extends BaseEntity {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "DATA_SOURCE_CONFIG_ID")
    private DataSourceConfig dataSourceConfig;

    @NotNull
    @Embedded
    private TaskSheet sheet;

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    private String owner;

    @NotNull
    private String cron;

    @NotNull
    private String query;

    @NotNull
    private boolean active;

    private LocalDateTime lastExecution;

    @Transient
    private LocalDateTime nextExecution;

    @NotNull
    private TaskStatus status;

}
