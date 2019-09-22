package com.exmyth.commons.validator.validation;

import com.exmyth.commons.validator.message.ConstraintViolation;
import com.exmyth.commons.validator.message.ValidationContext;

import java.lang.annotation.Annotation;

/**
 * @author exmyth
 * @date 2019-08-19 23:13
 *
 */
public interface Validator<A extends Annotation> {
    /**
     * 对象参数校验
     * @param annotation
     * @param context
     * @return
     */
    ConstraintViolation validate(A annotation, ValidationContext context);

    /**
     * 基本参数校验
     * @param context
     * @return
     */
    ConstraintViolation validate(ValidationContext context);
}
