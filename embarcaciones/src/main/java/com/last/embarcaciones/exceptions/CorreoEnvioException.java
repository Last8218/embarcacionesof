package com.last.embarcaciones.exceptions;

public class CorreoEnvioException extends RuntimeException {

    public CorreoEnvioException(String message, Throwable cause) {
        super(message, cause);
    }
}

