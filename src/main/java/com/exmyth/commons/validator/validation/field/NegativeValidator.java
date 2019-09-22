package com.exmyth.commons.validator.validation.field;

import com.exmyth.commons.validator.constant.ValidatorConstant;
import com.exmyth.commons.validator.constraint.field.Negative;
import com.exmyth.commons.validator.message.ConstraintViolation;
import com.exmyth.commons.validator.message.DefaultMessageInterpolator;
import com.exmyth.commons.validator.message.ValidationContext;
import com.exmyth.commons.validator.util.ValidatorUtil;
import com.exmyth.commons.validator.validation.FieldValidator;

/**
 * @author exmyth
 * @date 2019-07-18 06:54
 *
 */
public final class NegativeValidator implements FieldValidator<Negative> {
    @Negative
    private static final NegativeValidator instance = new NegativeValidator();

    private NegativeValidator() {}

    public static NegativeValidator getInstance() {
        return instance;
    }

    @Override
    public ConstraintViolation validate(Negative annotation, ValidationContext context) {
        boolean check = true;
        Object value = context.getValidatedValue();
        if (value == null) {
            check = false;
        } else if (value instanceof Long) {
            long num = ValidatorUtil.parseLong(value);
            check = num < 0;
        } else if (value instanceof Integer) {
            int num = ValidatorUtil.parseInteger(value);
            check = num < 0;
        } else if (value instanceof Float) {
            float num = ValidatorUtil.parseFloat(value);
            check = num < 0;
        } else if (value instanceof Double) {
            double num = ValidatorUtil.parseDouble(value);
            check = num < 0;
        } else if (value instanceof Byte) {
            byte num = ValidatorUtil.parseByte(value);
            check = num < 0;
        } else if (value instanceof Short) {
            short num = ValidatorUtil.parseShort(value);
            check = num < 0;
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
        Negative annotation = ValidatorUtil.getField(getClass(), ValidatorConstant.INSTANCE).getAnnotation(Negative.class);
        return validate(annotation, context);
    }
}
