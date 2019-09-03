package com.exmyth.commons.validator.validation.field;

import com.exmyth.commons.validator.constant.ValidatorConstant;
import com.exmyth.commons.validator.constraint.field.Email;
import com.exmyth.commons.validator.message.ConstraintViolation;
import com.exmyth.commons.validator.message.ValidationContext;
import com.exmyth.commons.validator.util.ValidatorUtil;
import com.exmyth.commons.validator.validation.FieldValidator;

import java.util.regex.Pattern;

/**
 * @author exmyth
 * @date 2019-07-18 09:46
 * @description
 */
public final class EmailValidator implements FieldValidator<Email> {
    private static final Pattern pattern = Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
    @Email
    private static final EmailValidator instance = new EmailValidator();

    public static EmailValidator getInstance() {
        return instance;
    }

    @Override
    public ConstraintViolation validate(Email annotation, ValidationContext context) {
        return validate(pattern, annotation, annotation.message(), context);
    }

    @Override
    public ConstraintViolation validate(ValidationContext context) {
        Email annotation = ValidatorUtil.getField(getClass(), ValidatorConstant.INSTANCE).getAnnotation(Email.class);
        return validate(annotation, context);
    }
}
