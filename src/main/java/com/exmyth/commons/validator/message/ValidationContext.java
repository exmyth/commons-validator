package com.exmyth.commons.validator.message;

import java.util.HashMap;
import java.util.Map;

/**
 * @author exmyth
 * @date 2019-07-17 15:20
 * @description
 */
public final class ValidationContext<T> implements MessageInterpolator.Context {
    private final Object validatedValue;
    private final Class<T> rootBeanType;
    private final String[] fieldNames;
    private final Map<String, Object> messageParameters;

    public ValidationContext(Object validatedValue,
                             Class<T> rootBeanType, String... fieldName) {
        this.validatedValue = validatedValue;
        this.rootBeanType = rootBeanType;
        this.fieldNames = fieldName;
        this.messageParameters = new HashMap<>();
    }

    @Override
    public Class<?> getRootBeanType() {
        return rootBeanType;
    }

    @Override
    public String[] getFieldNames() {
        return fieldNames;
    }

    @Override
    public Map<String, Object> getMessageParameters() {
        return messageParameters;
    }

    @Override
    public Object getValidatedValue() {
        return validatedValue;
    }

    public void putMessageParameterIfAbsent(String key, Object value) {
        this.messageParameters.putIfAbsent(key, value);
    }

    public Object getMessageParameter(String key) {
        return this.messageParameters.get(key);
    }
}
