package com.exmyth.commons.validator.validation.field;

import com.exmyth.commons.validator.constant.ValidatorConstant;
import com.exmyth.commons.validator.constraint.field.Range;
import com.exmyth.commons.validator.message.ConstraintViolation;
import com.exmyth.commons.validator.message.DefaultMessageInterpolator;
import com.exmyth.commons.validator.message.ValidationContext;
import com.exmyth.commons.validator.util.ValidatorUtil;
import com.exmyth.commons.validator.validation.FieldValidator;

import static com.exmyth.commons.validator.util.ValidatorUtil.*;

/**
 * @author exmyth
 * @date 2019-08-23 21:21
 *
 */
public final class RangeValidator implements FieldValidator<Range> {
    @Range(min = 0, max = Long.MAX_VALUE)
    private static final RangeValidator instance = new RangeValidator();

    private RangeValidator() {}

    public static RangeValidator getInstance() {
        return instance;
    }

    @Override
    public ConstraintViolation validate(Range annotation, ValidationContext context) {
        boolean check = true;
        context.putMessageParameterIfAbsent("min", annotation.min());
        context.putMessageParameterIfAbsent("max", annotation.max());
        context.putMessageParameterIfAbsent("step", annotation.step());
        long min = Long.parseLong(context.getMessageParameter("min").toString());
        long max = Long.parseLong(context.getMessageParameter("max").toString());
        long step = Long.parseLong(context.getMessageParameter("step").toString());

        Object validatedValue = context.getValidatedValue();
        if(validatedValue == null){
            check = false;
        } else if(validatedValue instanceof Long){
            long num = parseLong(validatedValue);
            check = min <= num && num <=max && ((num - min) % step == 0);
        } else if(validatedValue instanceof Integer){
            int num = parseInteger(validatedValue);
            check = min <= num && num <=max && ((num - min) % step == 0);
        } else if(validatedValue instanceof Float){
            float num = parseFloat(validatedValue);
            check = min <= num && num <=max && ((num - min) % step == 0);
        } else if(validatedValue instanceof Double){
            double num = parseDouble(validatedValue);
            check = min <= num && num <=max && ((num - min) % step == 0);
        } else if(validatedValue instanceof Byte){
            byte num = parseByte(validatedValue);
            check = min <= num && num <=max && ((num - min) % step == 0);
        } else if(validatedValue instanceof Short){
            short num = parseShort(validatedValue);
            check = min <= num && num <=max && ((num - min) % step == 0);
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
        Range annotation = ValidatorUtil.getField(getClass(), ValidatorConstant.INSTANCE).getAnnotation(Range.class);
        return validate(annotation, context);
    }
}
