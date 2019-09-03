package com.exmyth.commons.validator.message;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * @author exmyth
 * @date 2019-07-18 10:08
 * @description
 */
public final class ConstraintViolation {
    private String message;
    private String messageTemplate;
    private Annotation annotation;
    private Class rootBeanType;
    private String[] fieldNames = new String[0];
    private Object validatedValue;

    public String getMessage() {
        return message;
    }

    public String getMessageTemplate() {
        return messageTemplate;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public Class getRootBeanType() {
        return rootBeanType;
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
            return message;
        } else {
            return Arrays.toString(fieldNames) + message;
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    public final static class Builder{
        private String message;
        private String messageTemplate;
        private Annotation annotation;
        private Class rootBeanType;
        private String[] fieldNames = new String[0];
        private Object validatedValue;

        private Builder() {}

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder messageTemplate(String messageTemplate) {
            this.messageTemplate = messageTemplate;
            return this;
        }

        public Builder annotation(Annotation annotation) {
            this.annotation = annotation;
            return this;
        }

        public Builder rootBeanType(Class rootBeanType) {
            this.rootBeanType = rootBeanType;
            return this;
        }

        public Builder fieldNames(String[] fieldNames) {
            this.fieldNames = fieldNames;
            return this;
        }

        public Builder validatedValue(Object validatedValue) {
            this.validatedValue = validatedValue;
            return this;
        }

        public ConstraintViolation build() {
            ConstraintViolation violation = new ConstraintViolation();
            violation.message = message;
            violation.messageTemplate = messageTemplate;
            violation.annotation = annotation;
            violation.rootBeanType = rootBeanType;
            violation.fieldNames = fieldNames;
            violation.validatedValue = validatedValue;
            return violation;
        }
    }
}
