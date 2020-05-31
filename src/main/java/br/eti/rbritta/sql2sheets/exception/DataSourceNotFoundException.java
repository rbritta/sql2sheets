package br.eti.rbritta.sql2sheets.exception;

public class DataSourceNotFoundException extends RuntimeException {

    public DataSourceNotFoundException() {
        super("Data Source not found");
    }
}