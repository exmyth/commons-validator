package com.exmyth.commons.validator.constraint.field;

import com.exmyth.commons.validator.constraint.Scope;

import java.lang.annotation.*;

/**
 * @author exmyth
 * @date 2019-07-18 09:44
 * @description
 */
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Scope(String.class)
public @interface Email {
    //不是一个合法的电子邮件地址
    String message() default "{javax.validation.constraints.Email.message}";
}
