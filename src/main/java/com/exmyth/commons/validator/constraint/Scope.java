package com.exmyth.commons.validator.constraint;

import java.lang.annotation.*;

/**
 * @author exmyth
 * @date 2019-07-18 20:52
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
public @interface Scope {
    Class[] value();
}
