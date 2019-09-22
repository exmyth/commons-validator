package com.exmyth.commons.validator.constraint.field;

import com.exmyth.commons.validator.constraint.Scope;

import java.lang.annotation.*;

/**
 * @author exmyth
 * @date 2019-08-23 21:46
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Scope({String.class,Number.class})
public @interface Enum {
    //必须是{value}其中之一
    String message() default "{javax.validation.constraints.Enum.message}";
    String[] value();
}
