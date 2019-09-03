package com.exmyth.commons.validator.constraint.field;

import com.exmyth.commons.validator.constraint.Scope;

import java.lang.annotation.*;

/**
 * The annotated {@code CharSequence} must match the specified regular expression.
 * The regular expression follows the Java regular expression conventions
 * see {@link java.util.regex.Pattern}.
 * <p>
 * Accepts {@code CharSequence}. {@code null} elements are considered valid.
 *
 * @author exmyth
 * @date 2019-07-16 11:27
 * @description
 */
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Scope(String.class)
public @interface Pattern {
    String regexp();

    //需要匹配正则表达式"{regexp}"
    String message() default "{javax.validation.constraints.Pattern.message}";
}
