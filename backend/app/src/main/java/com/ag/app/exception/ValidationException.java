package com.ag.app.exception;

public class ValidationException extends RuntimeException {
    
    private String field;
    private String message;

    public ValidationException(String field, String message) {
        super(String.format("Erro de validação no campo '%s': %s", field, message));
        this.field = field;
        this.message = message;
    }

    public ValidationException(String message) {
        super(message);
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getValidationMessage() {
        return message;
    }
}
