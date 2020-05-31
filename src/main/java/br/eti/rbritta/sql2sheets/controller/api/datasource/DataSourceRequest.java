package br.eti.rbritta.sql2sheets.controller.api.datasource;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DataSourceRequest {

    private String name;
    private String url;
    private String username;
    private String password;

}
