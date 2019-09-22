package com.exmyth.commons.validator.validation.group;

import com.exmyth.commons.validator.constraint.group.NotAnyBlank;
import com.exmyth.commons.validator.message.ConstraintViolation;
import com.exmyth.commons.validator.message.DefaultMessageInterpolator;
import com.exmyth.commons.validator.message.ValidationContext;
import com.exmyth.commons.validator.util.ValidatorUtil;
import com.exmyth.commons.validator.validation.GroupValidator;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author exmyth
 * @date 2019-07-22 07:46
 *
 */
@NotAnyBlank
public final class NotAnyBlankValidator implements GroupValidator<NotAnyBlank> {
    private static final NotAnyBlankValidator instance = new NotAnyBlankValidator();

    public static NotAnyBlankValidator getInstance() {
        return instance;
    }

    private NotAnyBlankValidator() {}

    @Override
    public ConstraintViolation validate(NotAnyBlank annotation, ValidationContext context) {
        String[] nameList = new String[0];
        String[] valueList = new String[0];
        String[] fields = annotation.value();
        if(fields.length == 0){
            if(context.getRootBeanType() != null){
                List<Field> fieldList = ValidatorUtil.getAllFields(context.getRootBeanType()).stream()
                        .filter(field -> field.getType() == String.class)
                        .collect(Collectors.toList());
                valueList = new String[fieldList.size()];
                nameList = new String[fieldList.size()];
                for (int i = 0; i < fieldList.size(); i++) {
                    nameList[i] = fieldList.get(i).getName();
                    valueList[i] = ValidatorUtil.parseString(ValidatorUtil.getFieldValue(context.getValidatedValue(), fieldList.get(i)));
                }
            }
        } else {
            valueList = new String[fields.length];
            nameList = new String[fields.length];

            for (int i = 0; i < fields.length; i++) {
                Field field = ValidatorUtil.getField(context.getValidatedValue().getClass(), fields[i]);
                nameList[i] = field.getName();
                valueList[i] = ValidatorUtil.parseString(ValidatorUtil.getFieldValue(context.getValidatedValue(), field));
            }
        }

        ValidationContext ctx = new ValidationContext(valueList, context.getRootBeanType(), nameList);
        return validateValueList(annotation, ctx, valueList, nameList);
    }

    @Override
    public ConstraintViolation validate(ValidationContext context) {
        Object validatedValue = context.getValidatedValue();
        boolean check = false;
        String[] value = null;
        if(validatedValue != null) {
            if(ValidatorUtil.isInstance(validatedValue, String[].class)){
                value = (String[]) validatedValue;
            } else if(ValidatorUtil.isInstance(validatedValue, Object[].class)){
                Object[] arr = (Object[]) validatedValue;
                value = new String[arr.length];
                for (int i = 0; i < arr.length; i++) {
                    value[i] = ValidatorUtil.parseString(arr[i]);
                }
            } else{
                check = true;
            }
        }
        if(check){
            return null;
        }

        NotAnyBlank annotation = getClass().getAnnotation(NotAnyBlank.class);
        return validateValueList(annotation, context, value, context.getFieldNames());
    }

    private ConstraintViolation validateValueList(NotAnyBlank annotation, ValidationContext context, String[] valueList, String... fields) {
        if(!ValidatorUtil.isAnyBlank(valueList)){
            return null;
        }

        String message = annotation.message();
        if(message.indexOf(DefaultMessageInterpolator.LEFT_BRACE) > -1){
            message = DefaultMessageInterpolator.getInstance().interpolate(annotation.message(), null);
        }

        ConstraintViolation violation = ConstraintViolation.builder()
                .message(message)
                .messageTemplate(annotation.message())
                .annotation(annotation)
                .rootBeanType(context.getRootBeanType())
                .validatedValue(valueList)
                .fieldNames(fields).build();

        return violation;
    }
}
