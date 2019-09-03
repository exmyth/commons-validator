package com.exmyth.commons.validator;

import com.exmyth.commons.validator.bean.ValidationResult;
import com.exmyth.commons.validator.message.ConstraintViolation;
import com.exmyth.commons.validator.util.Validators;

import java.util.List;

/**
 * @author exmyth
 * @date 2019-07-30 14:38
 * @description
 */
public interface Validation {
    default ValidationResult<List<ConstraintViolation>> validate(boolean failFast){
        List<ConstraintViolation> list = Validators.validate(this, failFast);
        ValidationResult<List<ConstraintViolation>> response = ValidationResult.wrap(list.isEmpty(), list);
        return response;
    }
}
