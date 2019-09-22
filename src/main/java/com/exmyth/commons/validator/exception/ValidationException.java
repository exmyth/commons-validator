package com.exmyth.commons.validator.exception;

import java.util.Arrays;

/**
 * @author exmyth
 * @date 2019-07-17 15:02
 *
 */
public final class ValidationException extends RuntimeException {
    private final String[] fieldNames;
    private final Object validatedValue;
    public ValidationException(String message, String[] fieldNames, Object validatedValue) {
        super(message);
        this.fieldNames = fieldNames;
        this.validatedValue = validatedValue;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public Object getValidatedValue() {
        return validatedValue;
    }

    @Override
    public String toString() {
        if(fieldNames.length == 0){
            return getMessage();
        } else {
            return Arrays.toString(fieldNames) + getMessage();
        }
    }
}

