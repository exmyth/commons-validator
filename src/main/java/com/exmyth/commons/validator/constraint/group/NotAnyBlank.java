package com.exmyth.commons.validator.constraint.group;

import com.exmyth.commons.validator.constraint.Scope;

import java.lang.annotation.*;

/**
 * @author exmyth
 * @date 2019-07-21 07:33
 *
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(NotAnyBlanks.class)
@Scope(String.class)
public @interface NotAnyBlank {
    //任何一个不能为空
    String message() default "{javax.validation.constraints.NotAnyBlank.message}";
    String[] value() default {};
}
