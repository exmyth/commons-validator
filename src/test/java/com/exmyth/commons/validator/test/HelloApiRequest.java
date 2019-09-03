package com.exmyth.commons.validator.test;

import com.exmyth.commons.validator.Validation;
import com.exmyth.commons.validator.constraint.Customize;
import com.exmyth.commons.validator.constraint.field.Email;
import com.exmyth.commons.validator.constraint.field.Enum;
import com.exmyth.commons.validator.constraint.field.Length;
import com.exmyth.commons.validator.constraint.field.NotBlank;
import com.exmyth.commons.validator.constraint.group.NotAllBlank;
import com.exmyth.commons.validator.constraint.group.NotAnyBlank;
import com.exmyth.commons.validator.message.ConstraintViolation;
import com.exmyth.commons.validator.processor.inspect.Inspect;

/**
 * 编号1-8表示校验顺序, Customize优先于其他其他校验器
 * @author exmyth
 * @date 2019-07-17 16:24
 * @description
 */
//2 复合校验支持设置多个
@NotAnyBlank
@NotAnyBlank({"bizId", "product"})
//1 Customize优先于其他其他校验器
@Customize(method = "validateModel")
//3
@NotAllBlank(value = {"product", "email"}, message = "产品和邮箱不能同时为空")
public class HelloApiRequest implements Validation {
    //4 字段校验只能设置一个
    @Length(min = 2, max = 8)
    private String bizId = "hello world";
    //5
    @NotBlank
    private String product;
    //6
    @Email
    private String email;
    //8
    @NotBlank
    //7 Customize优先于其他其他校验器
    @Customize(method = "validateAppName")
    private String appName;

    //9
    @Enum({"1", "0", "2", "4"})
    private Integer category;

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Integer getCategory() {
        return category;
    }

    @Inspect(field = "category", validator = Enum.class, args = {"value", "1", "0", "2", "4"})
    public void setCategory(Integer category) {
        this.category = category;
    }

    public ConstraintViolation validateAppName(String appName){
        if("world".equals(appName)){
            return null;
        }

        String message = "值必须是world";
        Class<? extends HelloApiRequest> clazz = getClass();
        Customize annotation = clazz.getAnnotation(Customize.class);

        ConstraintViolation violation = ConstraintViolation.builder()
                .message(message)
                .annotation(annotation)
                .rootBeanType(clazz)
                .fieldNames(new String[]{"appName"})
                .validatedValue(appName)
                .build();

        return violation;
    }

    public ConstraintViolation validateModel(HelloApiRequest request){
        if("hello".equals(request.product)){
            return null;
        }

        String message = "产品必须是hello";
        Class<? extends HelloApiRequest> clazz = getClass();
        Customize annotation = clazz.getAnnotation(Customize.class);

        ConstraintViolation violation = ConstraintViolation.builder()
                .message(message)
                .fieldNames(new String[]{"product"})
                .validatedValue(request)
                .annotation(annotation)
                .rootBeanType(clazz)
                .build();

        return violation;
    }
}
