package br.eti.rbritta.sql2sheets.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private String message;
    private List<String> details;

}