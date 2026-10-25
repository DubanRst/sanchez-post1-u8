package com.example.auditoria.adapter.in.web;

import com.example.auditoria.domain.valueobject.TransicionInvalidaException;
import com.example.auditoria.usecase.HallazgoNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(HallazgoNotFoundException.class)
    public ResponseEntity<Map<String, String>> noEncontrado(HallazgoNotFoundException ex) {
        return ResponseEntity.status(404).body(Map.of("error", String.valueOf(ex.getMessage())));
    }

    @ExceptionHandler({TransicionInvalidaException.class, IllegalStateException.class,
            IllegalArgumentException.class, NullPointerException.class})
    public ResponseEntity<Map<String, String>> solicitudInvalida(RuntimeException ex) {
        return ResponseEntity.status(400).body(Map.of("error", String.valueOf(ex.getMessage())));
    }
}