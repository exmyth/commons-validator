package com.exmyth.commons.validator.validation.field;

import com.exmyth.commons.validator.constant.ValidatorConstant;
import com.exmyth.commons.validator.constraint.field.Max;
import com.exmyth.commons.validator.message.ConstraintViolation;
import com.exmyth.commons.validator.message.DefaultMessageInterpolator;
import com.exmyth.commons.validator.message.ValidationContext;
import com.exmyth.commons.validator.util.ValidatorUtil;
import com.exmyth.commons.validator.validation.FieldValidator;

/**
 * @author exmyth
 * @date 2019-07-18 06:52
 * @description
 */
public final class MaxValidator implements FieldValidator<Max> {
    @Max(value = 0)
    private static final MaxValidator instance = new MaxValidator();

    private MaxValidator() {}

    public static MaxValidator getInstance() {
        return instance;
    }

    @Override
    public ConstraintViolation validate(Max annotation, ValidationContext context) {
        boolean check = true;

        context.putMessageParameterIfAbsent("value", annotation.value());

        long value = Long.parseLong(context.getMessageParameter("value").toString());

        Object validatedValue = context.getValidatedValue();
        if(validatedValue == null){
            check = false;
        } else if(validatedValue instanceof Long){
            long num = ValidatorUtil.parseLong(validatedValue);
            check = num <= value;
        } else if(validatedValue instanceof Integer){
            int num = ValidatorUtil.parseInteger(validatedValue);
            check = num <= value;
        } else if(validatedValue instanceof Float){
            float num = ValidatorUtil.parseFloat(validatedValue);
            check = num <= value;
        } else if(validatedValue instanceof Double){
            double num = ValidatorUtil.parseDouble(validatedValue);
            check = num <= value;
        } else if(validatedValue instanceof Byte){
            byte num = ValidatorUtil.parseByte(validatedValue);
            check = num <= value;
        } else if(validatedValue instanceof Short){
            short num = ValidatorUtil.parseShort(validatedValue);
            check = num <= value;
        }
        if(check) {
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
        Max annotation = ValidatorUtil.getField(getClass(), ValidatorConstant.INSTANCE).getAnnotation(Max.class);
        return validate(annotation, context);
    }
}
