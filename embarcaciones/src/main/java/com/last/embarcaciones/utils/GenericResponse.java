package com.last.embarcaciones.utils;

public class GenericResponse<T> {

    private String type;
    private int rpta;
    private String message;
    private T body;

    // Constructor por defecto
    public GenericResponse() {
        this.type = "";
        this.rpta = 0;
        this.message = "";
        this.body = null;
    }

    // Constructor completo
    public GenericResponse(String type, int rpta, String message, T body) {
        this.type = type;
        this.rpta = rpta;
        this.message = message;
        this.body = body;
    }

    // Métodos estáticos de fábrica para respuestas comunes
    public static <T> GenericResponse<T> success(String message, T body) {
        return new GenericResponse<>("success", 1, message, body);
    }

    public static <T> GenericResponse<T> warning(String message) {
        return new GenericResponse<>("warning", 0, message, null);
    }

    public static <T> GenericResponse<T> error(String message) {
        return new GenericResponse<>("error", -1, message, null);
    }

    // Getters y setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRpta() {
        return rpta;
    }

    public void setRpta(int rpta) {
        this.rpta = rpta;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}