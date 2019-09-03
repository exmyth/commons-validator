package com.exmyth.commons.validator.constraint;

import java.lang.annotation.*;

/**
 * @author exmyth
 * @date 2019-08-26 17:43
 * @description
 */
@Documented
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Scope(Object.class)
public @interface Customize {
    String message() default "{javax.validation.constraints.Customize.message}";

    //method方法声明必须是 public ConstraintViolation 方法名(参数类型 参数名称)
    String method();
}
