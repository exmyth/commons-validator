package com.exmyth.commons.validator.validation.group;

import com.exmyth.commons.validator.constraint.group.NotAllBlank;
import com.exmyth.commons.validator.message.ConstraintViolation;
import com.exmyth.commons.validator.message.DefaultMessageInterpolator;
import com.exmyth.commons.validator.message.ValidationContext;
import com.exmyth.commons.validator.util.ValidatorUtil;
import com.exmyth.commons.validator.validation.GroupValidator;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import static com.exmyth.commons.validator.message.DefaultMessageInterpolator.LEFT_BRACE;

/**
 * @author exmyth
 * @date 2019-07-22 07:15
 *
 */
@NotAllBlank
public final class NotAllBlankValidator implements GroupValidator<NotAllBlank> {
    private static final NotAllBlankValidator instance = new NotAllBlankValidator();

    private NotAllBlankValidator() {}

    public static NotAllBlankValidator getInstance() {
        return instance;
    }

    @Override
    public ConstraintViolation validate(NotAllBlank annotation, ValidationContext context) {
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
                    valueList[i] = ValidatorUtil.parseString(ValidatorUtil.getFieldValue(context.getValidatedValue(), fieldList.get(i)));
                    nameList[i] = fieldList.get(i).getName();
                }
            }
        } else {
            valueList = new String[fields.length];
            nameList = new String[fields.length];

            for (int i = 0; i < fields.length; i++) {
                Field field = ValidatorUtil.getField(context.getValidatedValue().getClass(), fields[i]);
                valueList[i] = ValidatorUtil.parseString(ValidatorUtil.getFieldValue(context.getValidatedValue(), field));
                nameList[i] = field.getName();
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

        NotAllBlank annotation = getClass().getAnnotation(NotAllBlank.class);
        return validateValueList(annotation, context, value, context.getFieldNames());
    }

    private ConstraintViolation validateValueList(NotAllBlank annotation, ValidationContext context, String[] valueList, String... fields) {
        if(!ValidatorUtil.isAllBlank(valueList)){
            return null;
        }

        String message = annotation.message();
        if(message.indexOf(LEFT_BRACE) > -1){
            message = DefaultMessageInterpolator.getInstance().interpolate(annotation.message(), null);
        }

        ConstraintViolation violation = ConstraintViolation.builder()
                .message(message)
                .messageTemplate(annotation.message())
                .annotation(annotation)
                .rootBeanType(context.getRootBeanType())
                .fieldNames(fields)
                .validatedValue(valueList)
                .build();

        return violation;
    }
}
