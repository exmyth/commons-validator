package com.exmyth.commons.validator.constraint.field;

import com.exmyth.commons.validator.constraint.Scope;

import java.lang.annotation.*;

/**
 * The annotated element must not be {@code null}.
 * Accepts any type.
 *
 * @author exmyth
 * @date 2019-07-16 20:23
 * @description
 */
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Scope(Object.class)
public @interface NotNull {
    //不能为null
    String message() default "{javax.validation.constraints.NotNull.message}";
}
