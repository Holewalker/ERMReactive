package com.svalero.ermreactive.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorException {
    private int code;
    private String message;
    private Map<String, String> errors;

    public ErrorException(int code, String message){
        this.code = code;
        this.message = message;
        errors = new HashMap<>();
    }
}
