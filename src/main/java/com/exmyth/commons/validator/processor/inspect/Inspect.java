package com.exmyth.commons.validator.processor.inspect;

import java.lang.annotation.*;

/**
 * @author exmyth
 * @date 2019-07-23 07:01
 *
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
@Repeatable(Inspects.class)
public @interface Inspect {
    String message() default "{javax.validation.constraints.Inspect.message}";

    Class<? extends Annotation> validator();

    Bean bean () default Bean.GETTER;

    String[] field();

    String[] args() default {};
}
