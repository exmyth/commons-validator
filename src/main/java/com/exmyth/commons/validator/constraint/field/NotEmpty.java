package com.exmyth.commons.validator.constraint.field;

import com.exmyth.commons.validator.constraint.Scope;

import java.lang.annotation.*;
import java.util.Collection;
import java.util.Map;

/**
 * The annotated element must not be {@code null} nor empty. Supported types are:
 * <ul>
 * <li>{@code CharSequence} (length of character sequence is evaluated)</li>
 * <li>{@code Collection} (collection size is evaluated)</li>
 * <li>{@code Map} (map size is evaluated)</li>
 * <li>Array (array length is evaluated)</li>
 * </ul>
 *
 * @author exmyth
 * @date 2019-07-16 21:48
 * @description
 */
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Scope({Collection.class, Map.class})
public @interface NotEmpty {
    //不能为空
    String message() default "{javax.validation.constraints.NotEmpty.message}";
}
