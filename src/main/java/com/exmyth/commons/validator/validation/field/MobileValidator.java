package com.exmyth.commons.validator.validation.field;

import com.exmyth.commons.validator.constant.ValidatorConstant;
import com.exmyth.commons.validator.constraint.field.Mobile;
import com.exmyth.commons.validator.message.ConstraintViolation;
import com.exmyth.commons.validator.message.ValidationContext;
import com.exmyth.commons.validator.util.ValidatorUtil;
import com.exmyth.commons.validator.validation.FieldValidator;

import java.util.regex.Pattern;

/**
 * @author exmyth
 * @date 2019-07-18 06:53
 *
 */
public final class MobileValidator implements FieldValidator<Mobile> {
    private static final Pattern pattern = Pattern.compile("^(\\+?86-?)?1\\d{10}$");
    @Mobile
    private static final MobileValidator instance = new MobileValidator();

    private MobileValidator() {}

    public static MobileValidator getInstance() {
        return instance;
    }

    @Override
    public ConstraintViolation validate(Mobile annotation, ValidationContext context) {
        return validate(pattern, annotation, annotation.message(), context);
    }

    @Override
    public ConstraintViolation validate(ValidationContext context) {
        Mobile annotation = ValidatorUtil.getField(getClass(), ValidatorConstant.INSTANCE).getAnnotation(Mobile.class);
        return validate(annotation, context);
    }
}
