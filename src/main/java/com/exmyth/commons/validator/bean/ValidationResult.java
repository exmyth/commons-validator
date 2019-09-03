package com.exmyth.commons.validator.bean;

import java.io.Serializable;

/**
 * @author exmyth
 * @date 2019-07-31 10:53
 * @description
 */
public final class ValidationResult<T> implements Serializable {
    private static final long serialVersionUID = -5000368965447802133L;

    private boolean success;

    private T data;

    private ValidationResult() {
    }

    public static <T> ValidationResult<T> wrap(boolean success, T data) {
        ValidationResult response = new ValidationResult();
        response.success = success;
        response.data = data;
        return response;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ValidationResult{" +
                "success=" + success +
                ", data=" + data +
                '}';
    }
}
