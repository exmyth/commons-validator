package com.exmyth.commons.validator.constraint.group;

import com.exmyth.commons.validator.constraint.Scope;

import java.lang.annotation.*;

/**
 * @author exmyth
 * @date 2019-07-21 07:33
 * @description
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(NotAllBlanks.class)
@Scope(String.class)
public @interface NotAllBlank {
    //不能全部为空
    String message() default "{javax.validation.constraints.NotAllBlank.message}";

    String[] value() default {};
}
