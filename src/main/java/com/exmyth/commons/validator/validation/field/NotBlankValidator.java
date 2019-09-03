package com.exmyth.commons.validator.validation.field;

import com.exmyth.commons.validator.constant.ValidatorConstant;
import com.exmyth.commons.validator.constraint.field.NotBlank;
import com.exmyth.commons.validator.message.ConstraintViolation;
import com.exmyth.commons.validator.message.DefaultMessageInterpolator;
import com.exmyth.commons.validator.message.ValidationContext;
import com.exmyth.commons.validator.util.ValidatorUtil;
import com.exmyth.commons.validator.validation.FieldValidator;

/**
 * @author exmyth
 * @date 2019-07-16 06:47
 * @description
 */
public final class NotBlankValidator implements FieldValidator<NotBlank> {
    @NotBlank
    private static final NotBlankValidator instance = new NotBlankValidator();

    private NotBlankValidator() {}

    public static NotBlankValidator getInstance() {
        return instance;
    }

    @Override
    public ConstraintViolation validate(NotBlank annotation, ValidationContext context) {
        String value = ValidatorUtil.parseString(context.getValidatedValue());
        if (ValidatorUtil.isNotBlank(value)) {
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
        NotBlank annotation = ValidatorUtil.getField(getClass(), ValidatorConstant.INSTANCE).getAnnotation(NotBlank.class);
        return validate(annotation, context);
    }
}
