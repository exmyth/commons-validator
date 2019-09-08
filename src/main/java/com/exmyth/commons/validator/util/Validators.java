package com.exmyth.commons.validator.util;

import com.exmyth.commons.validator.Validation;
import com.exmyth.commons.validator.constraint.Customize;
import com.exmyth.commons.validator.constraint.Scope;
import com.exmyth.commons.validator.constraint.field.*;
import com.exmyth.commons.validator.constraint.field.Enum;
import com.exmyth.commons.validator.constraint.group.NotAllBlank;
import com.exmyth.commons.validator.constraint.group.NotAllBlanks;
import com.exmyth.commons.validator.constraint.group.NotAnyBlank;
import com.exmyth.commons.validator.constraint.group.NotAnyBlanks;
import com.exmyth.commons.validator.exception.ValidationException;
import com.exmyth.commons.validator.message.ConstraintViolation;
import com.exmyth.commons.validator.message.ValidationContext;
import com.exmyth.commons.validator.validation.FieldValidator;
import com.exmyth.commons.validator.validation.GroupValidator;
import com.exmyth.commons.validator.validation.field.*;
import com.exmyth.commons.validator.validation.group.NotAllBlankValidator;
import com.exmyth.commons.validator.validation.group.NotAnyBlankValidator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static com.exmyth.commons.validator.util.ValidatorUtil.isInstance;

/**
 * @author exmyth
 * @date 2019-07-16 09:09
 * @description
 */
public final class Validators {
    private static final Map<Class, FieldValidator> validatorMap = new LinkedHashMap<>(18);
    private static final List<Class> validatorClassList = new ArrayList<>(18);

    private static final Map<Class, GroupValidator> groupValidatorMap = new LinkedHashMap<>(2);
    private static final List<Class> groupValidatorClassList = new ArrayList<>(2);

    private static final Map<Class, Class> repeatableGroupMap = new LinkedHashMap<>(2);
    private Validators() {}

    static {
        //field
        validatorMap.put(Email.class,           EmailValidator.getInstance());
        validatorMap.put(Enum.class,            EnumValidator.getInstance());
        validatorMap.put(IdNumber.class,        IdNumberValidator.getInstance());
        validatorMap.put(Length.class,          LengthValidator.getInstance());
        validatorMap.put(Max.class,             MaxValidator.getInstance());
        validatorMap.put(Min.class,             MinValidator.getInstance());
        validatorMap.put(Mobile.class,          MobileValidator.getInstance());
        validatorMap.put(Negative.class,        NegativeValidator.getInstance());
        validatorMap.put(NegativeOrZero.class,  NegativeOrZeroValidator.getInstance());
        validatorMap.put(NotBlank.class,        NotBlankValidator.getInstance());
        validatorMap.put(NotEmpty.class,        NotEmptyValidator.getInstance());
        validatorMap.put(NotNull.class,         NotNullValidator.getInstance());
        validatorMap.put(Pattern.class,         PatternValidator.getInstance());
        validatorMap.put(Positive.class,        PositiveValidator.getInstance());
        validatorMap.put(PositiveOrZero.class,  PositiveOrZeroValidator.getInstance());
        validatorMap.put(Even.class,            EvenValidator.getInstance());
        validatorMap.put(Odd.class,             OddValidator.getInstance());
        validatorMap.put(Range.class,           RangeValidator.getInstance());
        validatorMap.put(Size.class,            SizeValidator.getInstance());

        validatorClassList.addAll(validatorMap.keySet().stream().collect(Collectors.toList()));

        //group
        groupValidatorMap.put(NotAnyBlank.class, NotAnyBlankValidator.getInstance());
        groupValidatorMap.put(NotAllBlank.class, NotAllBlankValidator.getInstance());

        groupValidatorClassList.addAll(groupValidatorMap.keySet().stream().collect(Collectors.toList()));

        repeatableGroupMap.put(NotAnyBlanks.class, NotAnyBlank.class);
        repeatableGroupMap.put(NotAllBlanks.class, NotAllBlank.class);
    }

    public static List<Class> getValidatorClassList() {
        return validatorClassList;
    }

    public static List<Class> getGroupValidatorClassList() {
        return groupValidatorClassList;
    }

    /**
     * 适用于手动校验变量是否合法,
     * 校验通过,返回null
     *
     * @param value 被校验值
     * @param validator 校验器
     * @param args 校验器参数
     * @return
     */
    public static ConstraintViolation validate(Object value, Class validator, Object... args) {
        ValidationContext context = new ValidationContext(value, null);
        if(args.length>0){
            if(validator == Enum.class){
                if(args.length > 1){
                    String[] argValue = new String[args.length - 1];
                    for (int i = 1; i < args.length; i++) {
                        argValue[i-1] = String.valueOf(args[i]);
                    }
                    context.putMessageParameterIfAbsent(String.valueOf(String.valueOf(args[0])), argValue);
                }
            } else {
                for (int i = 0; i < args.length-1; i=i+2) {
                    context.putMessageParameterIfAbsent(String.valueOf(args[i]), args[i+1]);
                }
            }
        }
        FieldValidator fieldValidator = validatorMap.get(validator);
        if(fieldValidator != null){
            return fieldValidator.validate(context);
        }

        GroupValidator groupValidator= groupValidatorMap.get(validator);
        if(groupValidator != null){
            return groupValidator.validate(context);
        }
        return null;
    }

    public static void validateOrThrow(Object param, boolean failFast) {
        if(ValidatorUtil.isInstance(param, Validation.class)){
            Validation value = (Validation) param;
            List<ConstraintViolation> list = validate(value, failFast);
            if(!list.isEmpty()){
                if(failFast){
                    throw new ValidationException(list.get(0).getMessage(), list.get(0).getFieldNames(), value);
                }
                Set<String> fieldNamesSet = new LinkedHashSet();
                list.forEach(item -> {
                    if(!ValidatorUtil.isEmpty(item.getFieldNames())){
                        fieldNamesSet.addAll(Arrays.asList(item.getFieldNames()));
                    }
                });
                String message = list.stream().map(ConstraintViolation::getMessage).reduce((s1, s2) -> s1 + ", " + s2).orElse("");
                throw new ValidationException(message, fieldNamesSet.toArray(new String[]{}), value);
            }
        }
    }

    /**
     * 适用于手动校验变量是否合法,
     * 校验失败,抛出ValidationException异常
     * 异常信息为校验器默认message
     *
     * @param value 被校验值
     * @param fieldName 被校验值的名称
     * @param validator 校验器
     * @param args 校验器参数
     */
    public static void validateOrThrow(Object value, String fieldName, Class validator, String... args) {
        validateOrThrow(value, new String[]{fieldName}, null, validator, args);
    }

    /**
     * 适用于手动校验变量是否合法,
     * 校验失败,抛出ValidationException异常,
     * 异常信息为message
     *
     * @param value 被校验值
     * @param fieldName 被校验值的名称
     * @param message 异常信息
     * @param validator 校验器
     * @param args 校验器参数
     */
    public static void validateOrThrow(Object value, String fieldName, String message, Class validator, String... args) {
        validateOrThrow(value, new String[]{fieldName}, message, validator, args);
    }

    /**
     * 适用于手动校验变量是否合法,
     * 校验失败,抛出ValidationException异常,
     * 异常信息为校验器默认message
     *
     * @param value 被校验值
     * @param fieldNames 被校验值的名称
     * @param validator 校验器
     * @param args 校验器参数
     */
    public static void validateOrThrow(Object value, String[] fieldNames, Class validator, String... args) {
        validateOrThrow(value, fieldNames, null, validator, args);
    }

    /**
     * 适用于手动校验变量是否合法,
     * 校验失败,抛出ValidationException异常,
     * 异常信息为校验器默认message
     *
     * @param value 被校验值
     * @param fieldNames 被校验值的名称
     * @param message 异常信息
     * @param validator 校验器
     * @param args 校验器参数
     */
    public static void validateOrThrow(Object value, String[] fieldNames, String message, Class validator, String... args) {
        ConstraintViolation violation = validate(value, validator, args);
        if(violation != null){
            if(ValidatorUtil.isBlank(message)){
                message = violation.getMessage();
            }
            throw new ValidationException(message, fieldNames, value);
        }
    }

    /**
     * 适用于手动校验对象是否合法
     * 校验通过,返回空List
     *
     * @param instance 被校验实例
     * @param failFast 校验失败是否立即返回
     * @return
     */
    public static List<ConstraintViolation> validate(Validation instance, boolean failFast) {
        List<ConstraintViolation> messageList = new ArrayList<>();

        Class<?> clazz = instance.getClass();

        //1.类级别validator校验
        boolean r1 = validateClassValidator(clazz, instance, messageList);
        if (failFast && !r1) {
            return messageList;
        }

        //2.字段分组校验
        boolean r2 = validateGroup(clazz, instance, messageList, failFast);
        if (failFast && !r2) {
            return messageList;
        }

        List<Field> fieldList = ValidatorUtil.getAllFields(clazz);
        for (Field field : fieldList){
            //3.字段级别validator校验
            boolean r3 = validateFieldValidator(clazz, field, instance, messageList);
            if(failFast && !r3){
                return messageList;
            }

            //4.字段校验
            boolean r4 = validateField(clazz, field, instance, messageList, failFast);
            if(failFast && !r4){
                return messageList;
            }
        }

        return messageList;
    }

    /**
     * 属性校验
     * @param clazz
     * @param field
     * @param instance
     * @param messageList
     * @param failFast
     * @return
     */
    private static boolean validateField(Class<?> clazz, Field field, Object instance, List<ConstraintViolation> messageList, boolean failFast) {
        List<Annotation> annotationList = Arrays.stream(field.getAnnotations())
                .filter(a -> validatorMap.containsKey(a.annotationType()))
                .collect(Collectors.toList());
        if(annotationList.size()>0){
            Object validatedValue = ValidatorUtil.getFieldValue(instance, field);
            ValidationContext context = new ValidationContext(validatedValue, clazz, field.getName());
            for (Annotation a : annotationList){
                Class[] scopeClazz = a.annotationType().getAnnotation(Scope.class).value();
                if(validatedValue != null && !isInstance(validatedValue, scopeClazz)){
                    continue;
                }
                ConstraintViolation violation = validatorMap.get(a.annotationType()).validate(a, context);
                if(violation != null){
                    messageList.add(violation);
                    if(failFast){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 字段 Validator 校验
     * @see Customize
     * @param clazz
     * @param field
     * @param instance
     * @param messageList
     * @return
     */
    private static boolean validateFieldValidator(Class<?> clazz, Field field, Object instance, List<ConstraintViolation> messageList) {
        Customize fieldMethodValidator = field.getAnnotation(Customize.class);
        if(fieldMethodValidator != null){
            String methodName = fieldMethodValidator.method();
            Method method = ValidatorUtil.getMethod(clazz, methodName, field.getType());
            if(method == null){
                throw new IllegalAccessError("public ConstraintViolation " + methodName + "("+clazz.getSimpleName()+" args) method not exists in "
                        + clazz.getTypeName());
            }
            Object result = ValidatorUtil.invokeMethod(method, instance, ValidatorUtil.getFieldValue(instance, field));
            if(result != null){
                messageList.add((ConstraintViolation)result);
                return false;
            }
        }
        return true;
    }

    /**
     * 分组校验
     * @param clazz
     * @param instance
     * @param messageList
     * @param failFast
     * @return
     */
    private static boolean validateGroup(Class<?> clazz, Object instance, List<ConstraintViolation> messageList, boolean failFast) {
        Annotation[] groupAnnotations = clazz.getAnnotations();
        List<Annotation> groupAnnotationList = new ArrayList(groupAnnotations.length);
        for (Annotation annotation : groupAnnotations){
            if(groupValidatorMap.containsKey(annotation.annotationType())){
                groupAnnotationList.add(annotation);
            } else if(repeatableGroupMap.containsKey(annotation.annotationType())){
                Annotation[] annotationsByType = clazz.getAnnotationsByType(repeatableGroupMap.get(annotation.annotationType()));
                for (Annotation type : annotationsByType){
                    groupAnnotationList.add(type);
                }
            }
        }
        if(groupAnnotationList.size() > 0){
            for (Annotation a : groupAnnotationList){
                ValidationContext context = new ValidationContext(instance, clazz, clazz.getSimpleName());
                ConstraintViolation violation = groupValidatorMap.get(a.annotationType()).validate(a, context);
                if(violation != null){
                    messageList.add(violation);
                    if(failFast){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 类 Validator 校验
     * @see Customize
     * @param clazz
     * @param instance
     * @param messageList
     * @return
     */
    private static boolean validateClassValidator(Class<?> clazz, Object instance, List<ConstraintViolation> messageList) {
        Customize methodValidator = clazz.getAnnotation(Customize.class);
        if(methodValidator != null){
            String methodName = methodValidator.method();
            Method method = ValidatorUtil.getMethod(clazz, methodName, clazz);
            if(method == null){
                throw new IllegalAccessError("public ConstraintViolation " + methodName + "("+clazz.getSimpleName()+" args){} method not exists in "
                        + clazz.getTypeName());
            }
            Object result = ValidatorUtil.invokeMethod(method, instance, instance);
            if(result != null){
                messageList.add((ConstraintViolation)result);
                return false;
            }
        }
        return true;
    }
}
