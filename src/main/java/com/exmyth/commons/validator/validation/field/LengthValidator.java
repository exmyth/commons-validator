package com.exmyth.commons.validator.validation.field;


import com.exmyth.commons.validator.constant.ValidatorConstant;
import com.exmyth.commons.validator.constraint.field.Length;
import com.exmyth.commons.validator.message.ConstraintViolation;
import com.exmyth.commons.validator.message.DefaultMessageInterpolator;
import com.exmyth.commons.validator.message.ValidationContext;
import com.exmyth.commons.validator.util.ValidatorUtil;
import com.exmyth.commons.validator.validation.FieldValidator;

/**
 * @author exmyth
 * @date 2019-07-16 19:39
 *
 */
public final class LengthValidator implements FieldValidator<Length> {
    @Length
    private static final LengthValidator instance = new LengthValidator();

    private LengthValidator() {}

    public static LengthValidator getInstance() {
        return instance;
    }

    @Override
    public ConstraintViolation validate(Length annotation, ValidationContext context) {
        String validatedValue = ValidatorUtil.parseString(context.getValidatedValue());

        context.putMessageParameterIfAbsent("min", annotation.min());
        context.putMessageParameterIfAbsent("max", annotation.max());

        int min = Integer.parseInt(context.getMessageParameter("min").toString());
        int max = Integer.parseInt(context.getMessageParameter("max").toString());
        if (min <= ValidatorUtil.length(validatedValue)
                && ValidatorUtil.length(validatedValue) <= max) {
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
        Length annotation = ValidatorUtil.getField(getClass(), ValidatorConstant.INSTANCE).getAnnotation(Length.class);
        return validate(annotation, context);
    }
}
