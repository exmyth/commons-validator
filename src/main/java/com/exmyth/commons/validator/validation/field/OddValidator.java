package com.exmyth.commons.validator.validation.field;

import com.exmyth.commons.validator.constant.ValidatorConstant;
import com.exmyth.commons.validator.constraint.field.Odd;
import com.exmyth.commons.validator.message.ConstraintViolation;
import com.exmyth.commons.validator.message.DefaultMessageInterpolator;
import com.exmyth.commons.validator.message.ValidationContext;
import com.exmyth.commons.validator.util.ValidatorUtil;
import com.exmyth.commons.validator.validation.FieldValidator;

import static com.exmyth.commons.validator.util.ValidatorUtil.*;

/**
 * @author exmyth
 * @date 2019-08-24 14:33
 * @description
 */
public final class OddValidator implements FieldValidator<Odd> {
    @Odd
    private static final OddValidator instance = new OddValidator();

    public static OddValidator getInstance() {
        return instance;
    }
    private OddValidator() {}

    @Override
    public ConstraintViolation validate(Odd annotation, ValidationContext context) {
        boolean check = true;
        Object value = context.getValidatedValue();
        if (value == null) {
            check = false;
        } else if (value instanceof Long) {
            long num = parseLong(value);
            check = num % 2 == 1;
        } else if (value instanceof Integer) {
            int num = parseInteger(value);
            check = num % 2 == 1;
        } else if (value instanceof Float) {
            float num = parseFloat(value);
            check = num % 2 == 1;
        } else if (value instanceof Double) {
            double num = parseDouble(value);
            check = num % 2 == 1;
        } else if (value instanceof Byte) {
            byte num = parseByte(value);
            check = num % 2 == 1;
        } else if (value instanceof Short) {
            short num = parseShort(value);
            check = num % 2 == 1;
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
        Odd annotation = ValidatorUtil.getField(getClass(), ValidatorConstant.INSTANCE).getAnnotation(Odd.class);
        return validate(annotation, context);
    }
}
