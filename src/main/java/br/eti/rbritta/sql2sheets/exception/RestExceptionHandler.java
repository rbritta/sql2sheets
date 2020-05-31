package br.eti.rbritta.sql2sheets.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
 
    @ExceptionHandler({ TaskNotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(Exception ex, WebRequest request) {
        final ErrorResponse error = new ErrorResponse(ex.getLocalizedMessage(), new ArrayList<>());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
 
    @ExceptionHandler({ InvalidTaskCronException.class, ConstraintViolationException.class, DataIntegrityViolationException.class })
    public ResponseEntity<Object> handleBadRequest(Exception ex, WebRequest request) {
        final ErrorResponse error = new ErrorResponse(ex.getLocalizedMessage(), new ArrayList<>());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}