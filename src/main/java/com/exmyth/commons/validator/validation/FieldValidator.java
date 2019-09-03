package com.exmyth.commons.validator.validation;

import com.exmyth.commons.validator.message.ConstraintViolation;
import com.exmyth.commons.validator.message.DefaultMessageInterpolator;
import com.exmyth.commons.validator.message.ValidationContext;
import com.exmyth.commons.validator.util.ValidatorUtil;

import java.lang.annotation.Annotation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 实现此接口的类，其字段需要进行校验
 * @author exmyth
 * @date 2019-07-15 06:32
 * @description
 */
public interface FieldValidator<A extends Annotation> extends Validator<A>{
    /**
     * 正则校验
     * @param pattern
     * @param annotation
     * @param messageTemplate
     * @param context
     * @return
     */
    default ConstraintViolation validate(Pattern pattern, A annotation, String messageTemplate, ValidationContext context){
        String value = ValidatorUtil.parseString(context.getValidatedValue());
        if(value != null){
            Matcher matcher = pattern.matcher(value);
            if (matcher.matches()) {
                return null;
            }
        }
        String message = DefaultMessageInterpolator.getInstance().interpolate(messageTemplate, context);

        ConstraintViolation violation = ConstraintViolation.builder()
                .message(message)
                .messageTemplate(messageTemplate)
                .annotation(annotation)
                .rootBeanType(context.getRootBeanType())
                .fieldNames(context.getFieldNames())
                .validatedValue(context.getValidatedValue())
                .build();

        return violation;
    }
}
