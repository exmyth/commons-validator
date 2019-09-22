package com.exmyth.commons.validator.test;

import com.exmyth.commons.validator.Validation;
import com.exmyth.commons.validator.bean.JSONObject;
import com.exmyth.commons.validator.constraint.field.*;
import com.exmyth.commons.validator.constraint.field.Enum;
import com.exmyth.commons.validator.constraint.group.NotAllBlank;
import com.exmyth.commons.validator.processor.constraint.Constraint;
import com.exmyth.commons.validator.processor.constraint.Valid;
import com.exmyth.commons.validator.processor.inspect.Bean;
import com.exmyth.commons.validator.processor.inspect.Inspect;
import com.exmyth.commons.validator.util.Validators;

import java.util.Map;

import static com.exmyth.commons.validator.processor.inspect.Bean.MAPPER;

/**
 * 注解校验使用举例
 * @author exmyth
 * @date 2019-08-21 11:10
 *
 */
public class ValidatorApi extends RequestApi{
    @Inspect(field = "userName", validator = NotBlank.class)
    @Inspect(field = "realName", validator = Length.class, args = {"min", "2", "max", "64"})
    @Inspect(field = {"address", "email"}, validator = NotAllBlank.class)
    @Inspect(field = "email", validator = Email.class, message = "电子邮件地址格式错误")
    @Inspect(field = "category", validator = Enum.class, args = {"value", "1", "0", "2", "4"})
    public void v1(String userName, String realName, String address, String email, Integer category){
    }

    @Inspect(field = "obj.mobile", validator = Mobile.class)//默认使用getXxx()获取被校验值
    @Inspect(field = "obj.age", validator = Range.class, args = {"min", "18", "max", "150"})
    @Inspect(field = "obj1", validator = NotNull.class)
    @Inspect(field = "obj2", validator = NotNull.class)
    @Inspect(bean = MAPPER, field = {"obj1.hello", "obj2.world"}, validator = NotAllBlank.class)
    @Inspect(bean = Bean.MAPPER, field = "obj1.hello", validator = Length.class, args = {"min", "2", "max", "4"})
    @Inspect(bean = Bean.MAPPER, field = "obj2.world", validator = NotBlank.class)//使用get(xxx)获取被校验值
    public void v2(HelloRequest obj, JSONObject obj1, Map<String, Object> obj2){
    }

    //@Enum, @Valid等作为方法参数注解使用,必须在方法上加上@Constraint
    @Constraint
    public void v3(@Enum(value = {"A", "B", "C"}, message = "姓名必须是可以枚举值 A, B, C") String realName,
                   @Min(8) Integer age,
                   @Length(min = 2, max = 64) String email){
    }

    @Constraint
    public void v4(@Valid HelloBody body){
    }

    static class HelloBody implements Validation {
        @NotBlank
        private String username;
        @Length(min = 2, max = 64)
        private String realName;

        public String getUsername() {
            return username;
        }

        public String getRealName() {
            return realName;
        }
    }

    /**
     * Inspect不支持继承,且子类方法的校验也是优先级优于父类方法校验
     * 如果需要优先父类方法校验,可以参考这个方法的实现方式
     * @param helloRequest
     */
    @Override
    //@Inspect(field = "helloRequest.mobile", validator = Mobile.class)
    public void request(RequestApi.HelloRequest helloRequest) {
        //先调用父类方法进行校验
        super.request(helloRequest);
        //在使用子类自定义校验规则
        Validators.validateOrThrow(helloRequest.getMobile(), "mobile", Mobile.class);
    }
}
