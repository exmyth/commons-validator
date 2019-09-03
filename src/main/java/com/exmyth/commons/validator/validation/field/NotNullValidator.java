package com.exmyth.commons.validator.validation.field;

import com.exmyth.commons.validator.constant.ValidatorConstant;
import com.exmyth.commons.validator.constraint.field.NotNull;
import com.exmyth.commons.validator.message.ConstraintViolation;
import com.exmyth.commons.validator.message.DefaultMessageInterpolator;
import com.exmyth.commons.validator.message.ValidationContext;
import com.exmyth.commons.validator.util.ValidatorUtil;
import com.exmyth.commons.validator.validation.FieldValidator;

/**
 * @author exmyth
 * @date 2019-07-18 06:56
 * @description
 */
public final class NotNullValidator implements FieldValidator<NotNull> {
    @NotNull
    private static final NotNullValidator instance = new NotNullValidator();

    private NotNullValidator() {}

    public static NotNullValidator getInstance() {
        return instance;
    }

    @Override
    public ConstraintViolation validate(NotNull annotation, ValidationContext context) {
        if (context.getValidatedValue() != null) {
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
        NotNull annotation = ValidatorUtil.getField(getClass(), ValidatorConstant.INSTANCE).getAnnotation(NotNull.class);
        return validate(annotation, context);
    }
}
