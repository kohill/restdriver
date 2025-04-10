package com.companyname.rest.driver.model;

import java.util.*;

public class RestError {
    private final Map<String, Object> unknownFields = new HashMap<>();
    private String errorCode;
    private String message;
    private String field;
    private List<RestError> errors;

    public RestError() {
    }

    public RestError(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public List<RestError> getErrors() {
        return errors;
    }

    public void setErrors(List<RestError> errors) {
        this.errors = errors;
    }

    public Map<String, Object> getUnknownFields() {
        return unknownFields;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("\n", RestError.class.getSimpleName() + "[", "]");
        if (getErrorCode() != null && getMessage() != null) {
            joiner.add(String.format("ErrorCode: %s - Message: %s", errorCode, message));
        }
        if (field != null && !field.isEmpty()) {
            joiner.add(String.format("Field: %s", field));
        }
        if (getErrors() != null) {
            getErrors().stream().forEach(restError -> String.format("ErrorCode: %s - Message: %s", errorCode, message));
        }

        if (getUnknownFields() != null && !getUnknownFields().isEmpty()) {
            getUnknownFields().forEach((s, o) -> String.format("Unknown Fields: %s - %s", s, o));
        }

        return joiner.toString();
    }

    public void setUnknownFields(String name, Object value) {
        unknownFields.put(name, value);
    }

    public RestError adjustInfo(String... args) {
        Arrays.asList(args).stream().forEach(s -> String.format(message, s));
        return this;
    }

    public enum ErrorCode {
        ERROR_403("403"), ERROR_404("404"), ERROR_422("422");

        private final String code;

        ErrorCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
}