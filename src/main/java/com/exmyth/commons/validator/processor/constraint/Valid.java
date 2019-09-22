package com.exmyth.commons.validator.processor.constraint;

import com.exmyth.commons.validator.Validation;
import com.exmyth.commons.validator.constraint.Scope;

import java.lang.annotation.*;

/**
 * @author exmyth
 * @date 2019-08-28 21:58
 *
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.SOURCE)
@Scope(Validation.class)
public @interface Valid {
}
