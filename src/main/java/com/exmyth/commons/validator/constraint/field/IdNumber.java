package com.exmyth.commons.validator.constraint.field;

import com.exmyth.commons.validator.constraint.Scope;

import java.lang.annotation.*;

/**
 * @author exmyth
 * @date 2019-07-16 20:25
 *
 */
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Scope(String.class)
public @interface IdNumber {
    //不是一个合法的身份证号码
    String message() default "{javax.validation.constraints.IdNumber.message}";
}
