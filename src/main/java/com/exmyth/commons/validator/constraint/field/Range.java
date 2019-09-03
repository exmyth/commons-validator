package com.exmyth.commons.validator.constraint.field;

import com.exmyth.commons.validator.constraint.Scope;

import java.lang.annotation.*;

/**
 * @author exmyth
 * @date 2019-08-23 17:30
 * @description
 */
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Scope({Long.class, Integer.class, Float.class, Double.class, Byte.class, Short.class})
public @interface Range {
    //需要在{min}和{max}之间, 步进{step}
    String message() default "{org.hibernate.validator.constraints.Range.message}";
    long min();
    long max();
    long step() default 1;
}
