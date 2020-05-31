package br.eti.rbritta.sql2sheets.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
public class TaskSheet {

    @NotNull
    @Column(name = "SHEET_ID")
    private String id;

    @NotNull
    @Column(name = "SHEET_AUTHORIZATION", length = 4000)
    private String authorization;

    @NotNull
    @Column(name = "SHEET_RANGE_DATA")
    private String rangeData;

    @Column(name = "SHEET_RANGE_TIMESTAMP")
    private String rangeTimestamp;

}
