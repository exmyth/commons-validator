package com.exmyth.commons.validator.constraint.field;

import com.exmyth.commons.validator.constraint.Scope;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Scope({Long.class, Integer.class, Float.class, Double.class, Byte.class, Short.class})
public @interface Odd {
    //必须是正数
    String message() default "{javax.validation.constraints.Odd.message}";
}
