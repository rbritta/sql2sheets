package br.eti.rbritta.sql2sheets.exception;

public class InvalidTaskCronException extends InvalidDataException {

    public InvalidTaskCronException() {
        super("Invalid Task Cron");
    }
}