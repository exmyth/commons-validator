package com.exmyth.commons.validator.validation.field;

import com.exmyth.commons.validator.constant.ValidatorConstant;
import com.exmyth.commons.validator.constraint.field.NotEmpty;
import com.exmyth.commons.validator.message.ConstraintViolation;
import com.exmyth.commons.validator.message.DefaultMessageInterpolator;
import com.exmyth.commons.validator.message.ValidationContext;
import com.exmyth.commons.validator.util.ValidatorUtil;
import com.exmyth.commons.validator.validation.FieldValidator;

import java.util.Collection;
import java.util.Map;

import static com.exmyth.commons.validator.util.ValidatorUtil.*;

/**
 * @author exmyth
 * @date 2019-07-18 06:55
 * @description
 */
public final class NotEmptyValidator implements FieldValidator<NotEmpty> {
    @NotEmpty
    private static final NotEmptyValidator instance = new NotEmptyValidator();

    private NotEmptyValidator() {}

    public static NotEmptyValidator getInstance() {
        return instance;
    }

    @Override
    public ConstraintViolation validate(NotEmpty annotation, ValidationContext context) {
        boolean check = true;
        Object value = context.getValidatedValue();
        if(value == null){
            check = false;
        } else if(value instanceof Collection){
            check = !isEmpty(parseCollection(context.getValidatedValue()));
        } else if(value instanceof Map){
            check = !isEmpty(parseMap(context.getValidatedValue()));
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
        NotEmpty annotation = ValidatorUtil.getField(getClass(), ValidatorConstant.INSTANCE).getAnnotation(NotEmpty.class);
        return validate(annotation, context);
    }
}
