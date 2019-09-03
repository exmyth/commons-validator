package com.exmyth.commons.validator.processor.inspect;

import java.lang.annotation.*;

/**
 * @author exmyth
 * @date 2019-07-23 07:01
 * @description
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Inspects {
    Inspect[] value();
}
