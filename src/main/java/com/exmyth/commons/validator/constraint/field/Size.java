package com.exmyth.commons.validator.constraint.field;

import com.exmyth.commons.validator.constraint.Scope;

import java.lang.annotation.*;
import java.util.Collection;
import java.util.Map;

/**
 * @author exmyth
 * @date 2019-08-30 00:05
 *
 */
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Scope({Collection.class, Map.class})
public @interface Size {
    String message() default "{javax.validation.constraints.Size.message}";
    /**
     * @return size the element must be higher or equal to
     */
    int min() default 0;

    /**
     * @return size the element must be lower or equal to
     */
    int max() default Integer.MAX_VALUE;
}
