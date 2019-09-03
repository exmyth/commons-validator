package com.exmyth.commons.validator.validation.field;

import com.exmyth.commons.validator.constant.ValidatorConstant;
import com.exmyth.commons.validator.constraint.field.Enum;
import com.exmyth.commons.validator.message.ConstraintViolation;
import com.exmyth.commons.validator.message.DefaultMessageInterpolator;
import com.exmyth.commons.validator.message.ValidationContext;
import com.exmyth.commons.validator.util.ValidatorUtil;
import com.exmyth.commons.validator.validation.FieldValidator;

import java.util.Arrays;

/**
 * @author exmyth
 * @date 2019-08-23 23:04
 * @description
 */
public final class EnumValidator implements FieldValidator<Enum> {
    @Enum({})
    private static final EnumValidator instance = new EnumValidator();

    public static EnumValidator getInstance() {
        return instance;
    }

    private EnumValidator() {}

    @Override
    public ConstraintViolation validate(Enum annotation, ValidationContext context) {
        boolean check;

        context.putMessageParameterIfAbsent("value", annotation.value());
        String validatedValue = ValidatorUtil.parseString(context.getValidatedValue());

        if(validatedValue == null){
            check = false;
        } else {
            String[] value = (String[]) context.getMessageParameter("value");
            check = Arrays.asList(value).contains(validatedValue);
        }

        if(check){
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
        Enum annotation = ValidatorUtil.getField(getClass(), ValidatorConstant.INSTANCE).getAnnotation(Enum.class);
        return validate(annotation, context);
    }
}
