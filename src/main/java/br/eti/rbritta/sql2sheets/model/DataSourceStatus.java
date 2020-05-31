package br.eti.rbritta.sql2sheets.model;

public enum DataSourceStatus {
    UNDEFINED,
    VALID,
    INVALID;

    public static DataSourceStatus of(boolean valid) {
        return valid ? VALID : INVALID;
    }
}