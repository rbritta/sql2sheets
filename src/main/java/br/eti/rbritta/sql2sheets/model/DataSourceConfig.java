package br.eti.rbritta.sql2sheets.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Setter
@Getter
@Entity
public class DataSourceConfig extends BaseEntity {

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    private String url;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private DataSourceStatus status;
}
