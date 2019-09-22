package com.exmyth.commons.validator.validation.field;

import com.exmyth.commons.validator.constant.ValidatorConstant;
import com.exmyth.commons.validator.constraint.field.Positive;
import com.exmyth.commons.validator.message.ConstraintViolation;
import com.exmyth.commons.validator.message.DefaultMessageInterpolator;
import com.exmyth.commons.validator.message.ValidationContext;
import com.exmyth.commons.validator.util.ValidatorUtil;
import com.exmyth.commons.validator.validation.FieldValidator;

import static com.exmyth.commons.validator.util.ValidatorUtil.*;

/**
 * @author exmyth
 * @date 2019-07-18 06:56
 *
 */
public final class PositiveValidator implements FieldValidator<Positive> {
    @Positive
    private static final PositiveValidator instance = new PositiveValidator();

    private PositiveValidator() {
    }

    public static PositiveValidator getInstance() {
        return instance;
    }

    @Override
    public ConstraintViolation validate(Positive annotation, ValidationContext context) {
        boolean check = true;
        Object value = context.getValidatedValue();
        if (value == null) {
            check = false;
        } else if (value instanceof Long) {
            long num = parseLong(value);
            check = num > 0;
        } else if (value instanceof Integer) {
            int num = parseInteger(value);
            check = num > 0;
        } else if (value instanceof Float) {
            float num = parseFloat(value);
            check = num > 0;
        } else if (value instanceof Double) {
            double num = parseDouble(value);
            check = num > 0;
        } else if (value instanceof Byte) {
            byte num = parseByte(value);
            check = num > 0;
        } else if (value instanceof Short) {
            short num = parseShort(value);
            check = num > 0;
        }
        if (check) {
            return null;
        }
        String message = DefaultMessageInterpolator.getInstance().interpolate(annotation.message(), context);

        ConstraintViolation violation = ConstraintViolation.builder()
                .message(message)
                .messageTemplate(annotation.message())
                .annotation(annotation)
                .rootBeanType(context.getRootBeanType())
                .fieldNames(context.getFieldNames())
                .validatedValue(context.getValidatedValue())
                .build();

        return violation;
    }

    @Override
    public ConstraintViolation validate(ValidationContext context) {
        Positive annotation = ValidatorUtil.getField(getClass(), ValidatorConstant.INSTANCE).getAnnotation(Positive.class);
        return validate(annotation, context);
    }
}
