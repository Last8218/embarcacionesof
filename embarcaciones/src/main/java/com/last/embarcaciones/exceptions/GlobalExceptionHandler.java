package com.last.embarcaciones.exceptions;

import com.last.embarcaciones.utils.AppConstants;
import com.last.embarcaciones.utils.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<GenericResponse<Object>> manejarEntityNotFoundException(EntityNotFoundException ex) {
        GenericResponse<Object> response = new GenericResponse<>(
                AppConstants.TIPO_RESTULT,
                AppConstants.RPTA_WARNING,
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse<Object>> manejarExcepcionesGenerales(Exception ex) {
        GenericResponse<Object> response = new GenericResponse<>(
                AppConstants.TIPO_RESTULT,
                AppConstants.RPTA_ERROR,
                AppConstants.OPERACION_ERRONEA,
                null
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
