package com.exmyth.commons.validator.validation.field;

import com.exmyth.commons.validator.constant.ValidatorConstant;
import com.exmyth.commons.validator.constraint.field.IdNumber;
import com.exmyth.commons.validator.message.ConstraintViolation;
import com.exmyth.commons.validator.message.ValidationContext;
import com.exmyth.commons.validator.util.ValidatorUtil;
import com.exmyth.commons.validator.validation.FieldValidator;

import java.util.regex.Pattern;

/**
 * @author exmyth
 * @date 2019-07-18 06:18
 * @description
 */
public final class IdNumberValidator implements FieldValidator<IdNumber> {
    private static final Pattern pattern = Pattern.compile("(^\\d{15}$)|(^\\d{17}([0-9]|X|x)$)");
    @IdNumber
    private static final IdNumberValidator instance = new IdNumberValidator();
    private IdNumberValidator() {}

    public static IdNumberValidator getInstance() {
        return instance;
    }

    @Override
    public ConstraintViolation validate(IdNumber annotation, ValidationContext context) {
        return validate(pattern, annotation, annotation.message(), context);
    }

    @Override
    public ConstraintViolation validate(ValidationContext context) {
        IdNumber annotation = ValidatorUtil.getField(getClass(), ValidatorConstant.INSTANCE).getAnnotation(IdNumber.class);
        return validate(annotation, context);
    }
}
