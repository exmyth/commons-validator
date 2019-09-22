package com.exmyth.commons.validator.validation.field;

import com.exmyth.commons.validator.constant.ValidatorConstant;
import com.exmyth.commons.validator.constraint.field.Pattern;
import com.exmyth.commons.validator.message.ConstraintViolation;
import com.exmyth.commons.validator.message.DefaultMessageInterpolator;
import com.exmyth.commons.validator.message.ValidationContext;
import com.exmyth.commons.validator.util.ValidatorUtil;
import com.exmyth.commons.validator.validation.FieldValidator;

import java.util.regex.Matcher;

/**
 * @author exmyth
 * @date 2019-07-16 17:57
 *
 */
public final class PatternValidator implements FieldValidator<Pattern> {
    @Pattern(regexp = "*")
    private static final PatternValidator instance = new PatternValidator();

    private PatternValidator() {}

    public static PatternValidator getInstance() {
        return instance;
    }

    @Override
    public ConstraintViolation validate(Pattern annotation, ValidationContext context) {
        String value = ValidatorUtil.parseString(context.getValidatedValue());
        context.putMessageParameterIfAbsent("regexp", annotation.regexp());
        if(value != null){
            String regexp = (String) context.getMessageParameter("regexp");
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regexp);
            Matcher matcher = pattern.matcher(value);
            if (matcher.matches()) {
                return null;
            }
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
        Pattern annotation = ValidatorUtil.getField(getClass(), ValidatorConstant.INSTANCE).getAnnotation(Pattern.class);
        return validate(annotation, context);
    }
}
