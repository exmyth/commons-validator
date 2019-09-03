package com.exmyth.commons.validator.constraint.group;

import com.exmyth.commons.validator.constraint.Scope;

import java.lang.annotation.*;

/**
 * @author exmyth
 * @date 2019-08-25 16:41
 * @description
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Scope(String.class)
public @interface NotAnyBlanks {
    NotAnyBlank[] value();
}
