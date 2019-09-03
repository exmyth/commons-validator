package com.exmyth.commons.validator.test;

import com.exmyth.commons.validator.bean.JSONObject;
import com.exmyth.commons.validator.bean.ValidationResult;
import com.exmyth.commons.validator.constraint.field.*;
import com.exmyth.commons.validator.constraint.field.Enum;
import com.exmyth.commons.validator.constraint.group.NotAllBlank;
import com.exmyth.commons.validator.constraint.group.NotAnyBlank;
import com.exmyth.commons.validator.exception.ValidationException;
import com.exmyth.commons.validator.message.ConstraintViolation;
import com.exmyth.commons.validator.util.Validators;

import java.util.HashMap;
import java.util.List;

/**
 * @author exmyth
 * @date 2019-07-17 07:31
 * @description
 */
public class HelloTest {

    public static void main(String[] args) {
        HelloTest test = new HelloTest();
        //对象校验(运行时校验)
        test.testValidator();
        System.out.println();
        //通用校验(运行时校验) 简单校验
        test.testValidateBasicVariable();
        System.out.println();
        //通用校验(运行时校验) 复合校验
        test.testGroupValidator();
        System.out.println();
        //通用校验(运行时校验) 异常校验
        test.testValidateOrThrow();

        //注解校验(编译器校验) 异常校验
        test.testConstraint();
    }

    private void testConstraint() {
        ValidatorApi validatorApi = new ValidatorApi();
        validatorApi.v1("", "", "", "", 0);
        validatorApi.v2(new RequestApi.HelloRequest(), new JSONObject(), new HashMap<String, Object>());
        validatorApi.v3("B", 18, "@");
        validatorApi.v4(new ValidatorApi.HelloBody());
        validatorApi.request(new RequestApi.HelloRequest());
    }

    public void testValidator() {
        HelloApiRequest helloApiRequest = new HelloApiRequest();
        ValidationResult<List<ConstraintViolation>> result = helloApiRequest.validate(false);
        System.out.println(result);
        result = helloApiRequest.validate(true);
        if(!result.isSuccess()){
            System.out.println(result.getData().get(0));
        }
    }

    private void testValidateOrThrow() {
        try {
            HelloApiRequest request = new HelloApiRequest();
            Validators.validateOrThrow(request, true);
        } catch (ValidationException e) {
            System.out.println(e);
        }

        try {
            Validators.validateOrThrow("hello/world@welcome.com", "email", Email.class);
        } catch (ValidationException e) {
            System.out.println(e);
        }

        try {
            Validators.validateOrThrow("hello/world@welcome.com", "idNumber", "身份证号码格式错误", IdNumber.class);
        } catch (ValidationException e) {
            System.out.println(e);
        }

        try {
            Validators.validateOrThrow(new String[]{"", null}, new String[]{"email", "idNumber"}, NotAllBlank.class);
        } catch (ValidationException e) {
            System.out.println(e);
        }

        try {
            Validators.validateOrThrow(new String[]{"", null}, new String[]{"email", "idNumber"}, "邮箱和身份证号码不能同时为空", NotAllBlank.class);
        } catch (ValidationException e) {
            System.out.println(e);
        }
    }

    private void testGroupValidator() {
        ConstraintViolation validate = Validators.validate(new String[]{null, ""}, NotAllBlank.class);
        System.out.println(validate);

        validate = Validators.validate(new String[]{"A", ""}, NotAnyBlank.class);
        System.out.println(validate);
    }

    private void testValidateBasicVariable() {
        ConstraintViolation violation = Validators.validate("hello.world@welcome.cn", Email.class);
        System.out.println(violation);

        violation = Validators.validate("hello/world@welcome.cn", Email.class);
        System.out.println(violation);

        violation = Validators.validate(3, Enum.class, "value", 1, 0, 2, 4);
        System.out.println(violation);

        violation = Validators.validate("350000199000000000A", IdNumber.class);
        System.out.println(violation);

        violation = Validators.validate("123", Length.class, "min", 4);
        System.out.println(violation);

        violation = Validators.validate("12345678", Length.class, "min", 4, "max", 6);
        System.out.println(violation);

        violation = Validators.validate("-12345678901", Mobile.class);
        System.out.println(violation);

        violation = Validators.validate(1, NegativeOrZero.class);
        System.out.println(violation);

        violation = Validators.validate(0, Negative.class);
        System.out.println(violation);

        violation = Validators.validate("", NotBlank.class);
        System.out.println(violation);

        violation = Validators.validate(null, NotNull.class);
        System.out.println(violation);

        violation = Validators.validate("0", Pattern.class, "regexp", "[A-Z]");
        System.out.println(violation);

        violation = Validators.validate(-1, PositiveOrZero.class);
        System.out.println(violation);

        violation = Validators.validate(0, Positive.class);
        System.out.println(violation);

        violation = Validators.validate(4, Range.class, "min", 2, "max", 8, "step", 3);
        System.out.println(violation);

        violation = Validators.validate(5, Even.class);
        System.out.println(violation);

        violation = Validators.validate(4, Odd.class);
        System.out.println(violation);

        violation = Validators.validate(null, Size.class, "min", 1);
        System.out.println(violation);
    }
}
