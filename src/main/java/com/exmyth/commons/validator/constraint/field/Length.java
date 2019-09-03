package com.exmyth.commons.validator.constraint.field;

import com.exmyth.commons.validator.constraint.Scope;

import java.lang.annotation.*;

/**
 * Validate that the string is between min and max included.
 *
 * @author exmyth
 * @date 2019-07-16 19:38
 * @description
 */
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Scope(String.class)
public @interface Length {
    int min() default 0;

    int max() default Integer.MAX_VALUE;

    //长度需要在{min}和{max}之间
    String message() default "{org.hibernate.validator.constraints.Length.message}";
}
