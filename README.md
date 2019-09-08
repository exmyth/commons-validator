# commons-validator

一个轻量的验证器

a validator without any dependency except jre

在业务功能实现中, 大多都会对输入参数做一些参数校验, 使用方式各不相同.
为了让大家从参数校验这种简单又琐碎的事情中抽身出来, 可以更多的关注业务实现, 因此开发了一个简单的参数校验器.
此校验器不依赖二方库和三方库, 使用简单, 口感纯厚, 无色无味, 居家旅行, 必备良药.

## 功能

注解校验(编译时生成校验代码)

通用校验(运行时校验)

```java
目前支持以下校验规则,如果仍无法满足你的需求,请issues留言
``` 

独立校验:Email, Enum, Even, IdNumber, Length, Max, Min, Mobile, Negative, NegativeOrZero, NotBlank, NotEmpty, NotNull, Odd, Pattern, Positive, PositiveOrZero, Range, Size

组合校验:NotAllBlank, NotAnyBlank

## 药引

```xml
<dependency>
  <groupId>com.exmyth</groupId>
  <artifactId>commons-validator</artifactId>
</dependency>
```

## 服用

使用方式


1)通用校验:com.exmyth.commons.validator.util.Validators 提供了一些通用校验方法

```java
/**
 * 适用于手动校验变量是否合法,
 * 校验通过,返回null
 *
 * @param value 被校验值
 * @param validator 校验器
 * @param args 校验器参数
 * @return
 */
public static ConstraintViolation validate(Object value, Class validator, Object... args);

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
public static void validateOrThrow(Object value, String fieldName, Class validator, String... args);

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
public static void validateOrThrow(Object value, String fieldName, String message, Class validator, String... args);

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
public static void validateOrThrow(Object value, String[] fieldNames, Class validator, String... args);

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
public static void validateOrThrow(Object value, String[] fieldNames, String message, Class validator, String... args);

/**
 * 适用于手动校验对象是否合法
 * 校验通过,返回空List
 *
 * @param instance 被校验实例
 * @param failFast 校验失败是否立即返回
 * @return
 */
public static List<ConstraintViolation> validate(Object instance, boolean failFast);
```
2)注解校验:支持@Constraint 配合 @NotBlank @Valid 等校验规则注解使用

```java 
@Constraint
private Object validate(@Enum(value = {"A", "B", "C"}, message = "姓名必须是可以枚举值 A, B, C") String name,
                       @Min(8) Integer num,
                       @Length(min = 2, max = 10) String email, @Valid HelloBody body){
    System.out.println(name);
    return name;
}
```

通过校验规则注解上的@Scope,可以查看到校验规则适用的被校验对象的类型.

校验规则具体参数信息请查看其注解的属性和说明

```java
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Scope(String.class)
public @interface Length {
    int min() default 0;

    int max() default Integer.MAX_VALUE;

    //长度需要在{min}和{max}之间
    String message() default "{org.hibernate.validator.constraints.Length.message}";
}
```

## 栗子

Talk is cheap. Show me the code

> 注解校验, 编译时生成校验代码
>
> [com.exmyth.commons.validator.test.RequestApi](https://github.com/exmyth/commons-validator/blob/master/src/test/java/com/exmyth/commons/validator/test/RequestApi.java)
>
> [com.exmyth.commons.validator.test.ValidatorApi](https://github.com/exmyth/commons-validator/blob/master/src/test/java/com/exmyth/commons/validator/test/ValidatorApi.java)
>
> _编译后的代码_

```java
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

public class ValidatorApi extends RequestApi {
    public ValidatorApi() {
    }

    public Object v1(String userName, String realName, String address, String email, Integer category) {
        Validators.validateOrThrow(userName, new String[]{"userName"}, "", NotBlank.class, new String[0]);
        Validators.validateOrThrow(realName, new String[]{"realName"}, "", Length.class, new String[]{"min", "2", "max", "4"});
        Validators.validateOrThrow(new Object[]{address, email}, new String[]{"address", "email"}, "", NotAllBlank.class, new String[0]);
        Validators.validateOrThrow(email, new String[]{"email"}, "电子邮件地址格式错误", Email.class, new String[0]);
        Validators.validateOrThrow(category, new String[]{"category"}, "", Enum.class, new String[]{"value", "1", "0", "2", "4"});
        Map<String, Object> result = new HashMap();
        result.put("userName", userName);
        result.put("realName", realName);
        result.put("address", address);
        result.put("email", email);
        System.out.println(result);
        return result;
    }

    public Object v2(ValidatorApi.HelloRequest obj, JSONObject obj1, Map<String, Object> obj2) {
        Validators.validateOrThrow(obj.getId(), new String[]{"id"}, "", Positive.class, new String[0]);
        Validators.validateOrThrow(obj.getName(), new String[]{"name"}, "", Enum.class, new String[]{"value", "A", "B", "C", "D"});
        Validators.validateOrThrow(obj1, new String[]{"obj1"}, "", NotNull.class, new String[0]);
        Validators.validateOrThrow(obj2, new String[]{"obj2"}, "", NotNull.class, new String[0]);
        Validators.validateOrThrow(new Object[]{obj1.get("hello"), obj2.get("world")}, new String[]{"hello", "world"}, "", NotAllBlank.class, new String[0]);
        Validators.validateOrThrow(obj1.get("hello"), new String[]{"hello"}, "", Length.class, new String[]{"min", "2", "max", "4"});
        Validators.validateOrThrow(obj2.get("world"), new String[]{"world"}, "", NotBlank.class, new String[0]);
        System.out.println(obj);
        return obj;
    }

    public Object v3(@Enum(value = {"A", "B", "C"},message = "姓名必须是可以枚举值 A, B, C") String name, @Min(8L) Integer num, @Length(min = 2,max = 10) String email) {
        Validators.validateOrThrow(name, new String[]{"name"}, "姓名必须是可以枚举值 A, B, C", Enum.class, new String[]{"value", "A", "B", "C"});
        Validators.validateOrThrow(num, new String[]{"num"}, "", Min.class, new String[]{"value", "8"});
        Validators.validateOrThrow(email, new String[]{"email"}, "", Length.class, new String[]{"min", "2", "max", "10"});
        System.out.println(name);
        return name;
    }

    public void v4(cn.tongdun.hello.web.RequestApi.HelloRequest helloRequest) {
        super.request(helloRequest);
        Validators.validateOrThrow(helloRequest.getMobile(), "mobile", Mobile.class, new String[0]);
    }

    class HelloRequest {
        private Integer id;
        private String name;

        HelloRequest() {
        }

        public Integer getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }
    }
}
```

## 校验信息

为了规范统一, 除了IdNumber等新增的验规则外,其他校验规则信息的message template均和validation-api,hibernate-validator命名规则保持一致

```properties
javax.validation.constraints.AssertFalse.message     = 只能为false
javax.validation.constraints.AssertTrue.message      = 只能为true
javax.validation.constraints.DecimalMax.message      = 必须小于或等于{value}
javax.validation.constraints.DecimalMin.message      = 必须大于或等于{value}
javax.validation.constraints.Digits.message          = 数字的值超出了允许范围(只允许在{integer}位整数和{fraction}位小数范围内)
javax.validation.constraints.Email.message           = 不是一个合法的电子邮件地址
javax.validation.constraints.Future.message          = 需要是一个将来的时间
javax.validation.constraints.FutureOrPresent.message = 需要是一个将来或现在的时间
javax.validation.constraints.IdNumber.message        = 不是一个合法的身份证号
javax.validation.constraints.Max.message             = 最大不能超过{value}
javax.validation.constraints.Min.message             = 最小不能小于{value}
javax.validation.constraints.Mobile.message          = 不是一个合法的手机号
javax.validation.constraints.Negative.message        = 必须是负数
javax.validation.constraints.NegativeOrZero.message  = 必须是负数或零
javax.validation.constraints.NotAllBlank.message     = 不能全部为空
javax.validation.constraints.NotAnyBlank.message     = 任何一个不能为空
javax.validation.constraints.NotBlank.message        = 不能为空
javax.validation.constraints.NotEmpty.message        = 不能为空
javax.validation.constraints.NotNull.message         = 不能为null
javax.validation.constraints.Null.message            = 必须为null
javax.validation.constraints.Past.message            = 需要是一个过去的时间
javax.validation.constraints.PastOrPresent.message   = 需要是一个过去或现在的时间
javax.validation.constraints.Pattern.message         = 需要匹配正则表达式"{regexp}"
javax.validation.constraints.Positive.message        = 必须是正数
javax.validation.constraints.PositiveOrZero.message  = 必须是正数或零
javax.validation.constraints.Size.message            = 个数必须在{min}和{max}之间

org.hibernate.validator.constraints.CreditCardNumber.message        = 不合法的信用卡号码
org.hibernate.validator.constraints.Currency.message                = 不合法的货币 (必须是{value}其中之一)
org.hibernate.validator.constraints.EAN.message                     = 不合法的{type}条形码
org.hibernate.validator.constraints.Email.message                   = 不是一个合法的电子邮件地址
org.hibernate.validator.constraints.ISBN.message                    = 不是一个合法的国际标准书号
org.hibernate.validator.constraints.Length.message                  = 长度需要在{min}和{max}之间
org.hibernate.validator.constraints.CodePointLength.message         = 长度需要在{min}和{max}之间
org.hibernate.validator.constraints.LuhnCheck.message               = ${validatedValue}的校验码不合法, Luhn模10校验和不匹配
org.hibernate.validator.constraints.Mod10Check.message              = ${validatedValue}的校验码不合法, 模10校验和不匹配
org.hibernate.validator.constraints.Mod11Check.message              = ${validatedValue}的校验码不合法, 模11校验和不匹配
org.hibernate.validator.constraints.ModCheck.message                = ${validatedValue}的校验码不合法, ${modType}校验和不匹配
org.hibernate.validator.constraints.NotBlank.message                = 不能为空
org.hibernate.validator.constraints.NotEmpty.message                = 不能为空
org.hibernate.validator.constraints.ParametersScriptAssert.message  = 执行脚本表达式"{script}"没有返回期望结果
org.hibernate.validator.constraints.Range.message                   = 需要在{min}和{max}之间
org.hibernate.validator.constraints.SafeHtml.message                = 可能有不安全的HTML内容
org.hibernate.validator.constraints.ScriptAssert.message            = 执行脚本表达式"{script}"没有返回期望结果
org.hibernate.validator.constraints.URL.message                     = 需要是一个合法的URL

org.hibernate.validator.constraints.time.DurationMax.message        = 必须小于${inclusive == true ? '或等于' : ''}${days == 0 ? '' : days += '天'}${hours == 0 ? '' : hours += '小时'}${minutes == 0 ? '' : minutes += '分钟'}${seconds == 0 ? '' : seconds += '秒'}${millis == 0 ? '' : millis += '毫秒'}${nanos == 0 ? '' : nanos += '纳秒'}
org.hibernate.validator.constraints.time.DurationMin.message        = 必须大于${inclusive == true ? '或等于' : ''}${days == 0 ? '' : days += '天'}${hours == 0 ? '' : hours += '小时'}${minutes == 0 ? '' : minutes += '分钟'}${seconds == 0 ? '' : seconds += '秒'}${millis == 0 ? '' : millis += '毫秒'}${nanos == 0 ? '' : nanos += '纳秒'}
```

## 注意事项
由于@Inspect 使用策略 RetentionPolicy.SOURCE(注解只保留在源文件，当Java文件编译成class文件的时候，注解被遗弃)
因此不支持校验规则继承,  即父类方法标注的注解校验规则, 子类方法无法直接使用.
