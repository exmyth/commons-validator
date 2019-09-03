package com.exmyth.commons.validator.validation.field;

import com.exmyth.commons.validator.constant.ValidatorConstant;
import com.exmyth.commons.validator.constraint.field.Size;
import com.exmyth.commons.validator.message.ConstraintViolation;
import com.exmyth.commons.validator.message.DefaultMessageInterpolator;
import com.exmyth.commons.validator.message.ValidationContext;
import com.exmyth.commons.validator.util.ValidatorUtil;
import com.exmyth.commons.validator.validation.FieldValidator;

import java.util.Collection;
import java.util.Map;

/**
 * @author exmyth
 * @date 2019-07-18 06:55
 * @description
 */
public final class SizeValidator implements FieldValidator<Size> {
    @Size
    private static final SizeValidator instance = new SizeValidator();

    private SizeValidator() {}

    public static SizeValidator getInstance() {
        return instance;
    }

    @Override
    public ConstraintViolation validate(Size annotation, ValidationContext context) {
        boolean check = false;
        Object value = context.getValidatedValue();
        context.putMessageParameterIfAbsent("min", annotation.min());
        context.putMessageParameterIfAbsent("max", annotation.max());
        long min = Long.parseLong(context.getMessageParameter("min").toString());
        long max = Long.parseLong(context.getMessageParameter("max").toString());
        if(value == null){
            check = min == 0;
        } else if(value instanceof Collection){
            Collection collection = ValidatorUtil.parseCollection(value);
            check = min <= collection.size() && collection.size() <= max;
        } else if(value instanceof Map){
            Map map = ValidatorUtil.parseMap(value);
            check = min <= map.size() && map.size() <= max;
        } else if(ValidatorUtil.isInstance(value, Object[].class)) {
            Object[] objects = (Object[]) value;
            check = min <= objects.length && objects.length <= max;
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
        Size annotation = ValidatorUtil.getField(getClass(), ValidatorConstant.INSTANCE).getAnnotation(Size.class);
        return validate(annotation, context);
    }
}
