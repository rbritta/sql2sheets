package br.eti.rbritta.sql2sheets.exception;

public class TaskNotFoundException extends RuntimeException {
 
    public TaskNotFoundException() {
        super("Task not found");
    }
}