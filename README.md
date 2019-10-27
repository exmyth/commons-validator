# commons-validator

a validator without any dependency except jre  [中文](https://github.com/exmyth/commons-validator/blob/master/README_zh_CN.md)

In the implementation of business functions, most of them will do some parameter verification on the input parameters, and the usage is different.
In order to let everyone pull out from the simple and trivial things of parameter verification, we can pay more attention to the business implementation, so we developed a simple parameter checker.
This verifier does not depend on the two-party library and the three-party library. It is simple to use, has a pure taste, is colorless and tasteless, travels at home, and is a must-have medicine.

## Feature

> Annotation verification (generating verification code when compile)

> General verification (runtime check)


```html
Currently support the following verification rules, if you still can not meet your needs, please issue a message, I will maintain it regularly 

Independent check: Email, Enum, Even, IdNumber, Length, Max, Min, Mobile, Negative, NegativeOrZero, NotBlank, NotEmpty, NotNull, Odd, Pattern, Positive, PositiveOrZero, Range, Size

Compose check: NotAllBlank, NotAnyBlank
```

## Dependency
[last version](https://search.maven.org/search?q=g:com.github.exmyth%20AND%20a:commons-validator&core=gav)
```xml
<dependency>
  <groupId>com.github.exmyth</groupId>
  <artifactId>commons-validator</artifactId>
  <version>0.0.1</version>
</dependency>

implementation 'com.github.exmyth:commons-validator:0.0.1'
```

## Usage



1)General verification: com.exmyth.commons.validator.util.Validators Provides some general verification methods


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
2)Annotation verification:suport annotation @NotBlank @Valid and others with @Constraint

```java 
@Constraint
private Object validate(@Enum(value = {"A", "B", "C"}, message = "姓名必须是可以枚举值 A, B, C") String name,
                       @Min(8) Integer num,
                       @Length(min = 2, max = 10) String email, @Valid HelloBody body){
    System.out.println(name);
    return name;
}

class HelloBody implements Validation {
    @NotBlank
    private String username;
    @Length(min = 2, max = 64)
    private String realName;
}
```

By @Scope on the verification rule annotation, you can see the type of the checked object to which the validation rule applies.

Please check the properties and description of the annotation, for the specific parameter information of the verification rule

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

## Example

Talk is cheap. Show me the code

> [com.exmyth.commons.validator.test.RequestApi](https://github.com/exmyth/commons-validator/blob/master/src/test/java/com/exmyth/commons/validator/test/RequestApi.java)
>
> [com.exmyth.commons.validator.test.ValidatorApi](https://github.com/exmyth/commons-validator/blob/master/src/test/java/com/exmyth/commons/validator/test/ValidatorApi.java)
>
> _Compiled code_

```java
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

public class ValidatorApi extends RequestApi {
    public ValidatorApi() {
    }

    public void v1(String userName, String realName, String address, String email, Integer category) {
        Validators.validateOrThrow(userName, new String[]{"userName"}, "", NotBlank.class, new String[0]);
        Validators.validateOrThrow(realName, new String[]{"realName"}, "", Length.class, new String[]{"min", "2", "max", "64"});
        Validators.validateOrThrow(new Object[]{address, email}, new String[]{"address", "email"}, "", NotAllBlank.class, new String[0]);
        Validators.validateOrThrow(email, new String[]{"email"}, "电子邮件地址格式错误", Email.class, new String[0]);
        Validators.validateOrThrow(category, new String[]{"category"}, "", Enum.class, new String[]{"value", "1", "0", "2", "4"});
    }

    public void v2(HelloRequest obj, JSONObject obj1, Map<String, Object> obj2) {
        Validators.validateOrThrow(obj.getMobile(), new String[]{"mobile"}, "", Mobile.class, new String[0]);
        Validators.validateOrThrow(obj.getAge(), new String[]{"age"}, "", Range.class, new String[]{"min", "18", "max", "150"});
        Validators.validateOrThrow(obj1, new String[]{"obj1"}, "", NotNull.class, new String[0]);
        Validators.validateOrThrow(obj2, new String[]{"obj2"}, "", NotNull.class, new String[0]);
        Validators.validateOrThrow(new Object[]{obj1.get("hello"), obj2.get("world")}, new String[]{"hello", "world"}, "", NotAllBlank.class, new String[0]);
        Validators.validateOrThrow(obj1.get("hello"), new String[]{"hello"}, "", Length.class, new String[]{"min", "2", "max", "4"});
        Validators.validateOrThrow(obj2.get("world"), new String[]{"world"}, "", NotBlank.class, new String[0]);
    }

    public void v3(@Enum(value = {"A", "B", "C"},message = "姓名必须是可以枚举值 A, B, C") String realName, @Min(8L) Integer age, @Length(min = 2,max = 64) String email) {
        Validators.validateOrThrow(realName, new String[]{"realName"}, "姓名必须是可以枚举值 A, B, C", Enum.class, new String[]{"value", "A", "B", "C"});
        Validators.validateOrThrow(age, new String[]{"age"}, "", Min.class, new String[]{"value", "8"});
        Validators.validateOrThrow(email, new String[]{"email"}, "", Length.class, new String[]{"min", "2", "max", "64"});
    }

    public void v4(ValidatorApi.HelloBody body) {
        Validators.validateOrThrow(body, true);
    }

    public void request(HelloRequest helloRequest) {
        super.request(helloRequest);
        Validators.validateOrThrow(helloRequest.getMobile(), "mobile", Mobile.class, new String[0]);
    }

    static class HelloBody implements Validation {
        @NotBlank
        private String username;
        @Length(
            min = 2,
            max = 64
        )
        private String realName;

        HelloBody() {
        }

        public String getUsername() {
            return this.username;
        }

        public String getRealName() {
            return this.realName;
        }
    }
}
```

## Validation

In order to standardize and unify, in addition to the newly added rules such as IdNumber, the message template of other verification rule information is consistent with the validation-api, hibernate-validator naming rules.

```properties
javax.validation.constraints.AssertFalse.message     = must be false
javax.validation.constraints.AssertTrue.message      = must be true
javax.validation.constraints.DecimalMax.message      = must be less than ${inclusive == true ? 'or equal to ' : ''}{value}
javax.validation.constraints.DecimalMin.message      = must be greater than ${inclusive == true ? 'or equal to ' : ''}{value}
javax.validation.constraints.Digits.message          = numeric value out of bounds (<{integer} digits>.<{fraction} digits> expected)
javax.validation.constraints.Email.message           = must be a well-formed email address
javax.validation.constraints.Enum.message            = must be one of {value}
javax.validation.constraints.Future.message          = must be a future date
javax.validation.constraints.FutureOrPresent.message = must be a date in the present or in the future
javax.validation.constraints.IdNumber.message        = must be a well-formed identity number
javax.validation.constraints.Inspect.message         = must be a well-formed value
javax.validation.constraints.Max.message             = must be less than or equal to {value}
javax.validation.constraints.Min.message             = must be greater than or equal to {value}
javax.validation.constraints.Mobile.message          = must be a well-formed mobile number
javax.validation.constraints.Negative.message        = must be less than 0
javax.validation.constraints.NegativeOrZero.message  = must be less than or equal to 0
javax.validation.constraints.NotAllBlank.message     = must not be all blank
javax.validation.constraints.NotAnyBlank.message     = must not be any blank
javax.validation.constraints.NotBlank.message        = must not be blank
javax.validation.constraints.NotEmpty.message        = must not be empty
javax.validation.constraints.NotNull.message         = must not be null
javax.validation.constraints.Null.message            = must be null
javax.validation.constraints.Past.message            = must be a past date
javax.validation.constraints.PastOrPresent.message   = must be a date in the past or in the present
javax.validation.constraints.Pattern.message         = must match "{regexp}"
javax.validation.constraints.Positive.message        = must be greater than 0
javax.validation.constraints.PositiveOrZero.message  = must be greater than or equal to 0
javax.validation.constraints.Even.message            = must be even
javax.validation.constraints.Odd.message             = must be odd
javax.validation.constraints.Size.message            = size must be between {min} and {max}


org.hibernate.validator.constraints.CreditCardNumber.message        = invalid credit card number
org.hibernate.validator.constraints.Currency.message                = invalid currency (must be one of {value})
org.hibernate.validator.constraints.EAN.message                     = invalid {type} barcode
org.hibernate.validator.constraints.Email.message                   = not a well-formed email address
org.hibernate.validator.constraints.ISBN.message                    = invalid ISBN
org.hibernate.validator.constraints.Length.message                  = length must be between {min} and {max}
org.hibernate.validator.constraints.CodePointLength.message         = length must be between {min} and {max}
org.hibernate.validator.constraints.LuhnCheck.message               = The check digit for ${validatedValue} is invalid, Luhn Modulo 10 checksum failed
org.hibernate.validator.constraints.Mod10Check.message              = The check digit for ${validatedValue} is invalid, Modulo 10 checksum failed
org.hibernate.validator.constraints.Mod11Check.message              = The check digit for ${validatedValue} is invalid, Modulo 11 checksum failed
org.hibernate.validator.constraints.ModCheck.message                = The check digit for ${validatedValue} is invalid, ${modType} checksum failed
org.hibernate.validator.constraints.NotBlank.message                = may not be empty
org.hibernate.validator.constraints.NotEmpty.message                = may not be empty
org.hibernate.validator.constraints.ParametersScriptAssert.message  = script expression "{script}" didn't evaluate to true
org.hibernate.validator.constraints.Range.message                   = must be between {min} and {max} step {step}
org.hibernate.validator.constraints.SafeHtml.message                = may have unsafe html content
org.hibernate.validator.constraints.ScriptAssert.message            = script expression "{script}" didn't evaluate to true
org.hibernate.validator.constraints.UniqueElements.message          = must only contain unique elements
org.hibernate.validator.constraints.URL.message                     = must be a valid URL

org.hibernate.validator.constraints.br.CNPJ.message                 = invalid Brazilian corporate taxpayer registry number (CNPJ)
org.hibernate.validator.constraints.br.CPF.message                  = invalid Brazilian individual taxpayer registry number (CPF)
org.hibernate.validator.constraints.br.TituloEleitoral.message      = invalid Brazilian Voter ID card number

org.hibernate.validator.constraints.pl.REGON.message                = Invalid Polish Taxpayer Identification Number (REGON)
org.hibernate.validator.constraints.pl.NIP.message                  = Invalid VAT Identification Number (NIP)
org.hibernate.validator.constraints.pl.PESEL.message                = Invalid Polish National Identification Number (PESEL)

org.hibernate.validator.constraints.time.DurationMax.message        = must be shorter than${inclusive == true ? ' or equal to' : ''}${days == 0 ? '' : days == 1 ? ' 1 day' : ' ' += days += ' days'}${hours == 0 ? '' : hours == 1 ? ' 1 hour' : ' ' += hours += ' hours'}${minutes == 0 ? '' : minutes == 1 ? ' 1 minute' : ' ' += minutes += ' minutes'}${seconds == 0 ? '' : seconds == 1 ? ' 1 second' : ' ' += seconds += ' seconds'}${millis == 0 ? '' : millis == 1 ? ' 1 milli' : ' ' += millis += ' millis'}${nanos == 0 ? '' : nanos == 1 ? ' 1 nano' : ' ' += nanos += ' nanos'}
org.hibernate.validator.constraints.time.DurationMin.message        = must be longer than${inclusive == true ? ' or equal to' : ''}${days == 0 ? '' : days == 1 ? ' 1 day' : ' ' += days += ' days'}${hours == 0 ? '' : hours == 1 ? ' 1 hour' : ' ' += hours += ' hours'}${minutes == 0 ? '' : minutes == 1 ? ' 1 minute' : ' ' += minutes += ' minutes'}${seconds == 0 ? '' : seconds == 1 ? ' 1 second' : ' ' += seconds += ' seconds'}${millis == 0 ? '' : millis == 1 ? ' 1 milli' : ' ' += millis += ' millis'}${nanos == 0 ? '' : nanos == 1 ? ' 1 nano' : ' ' += nanos += ' nanos'}

```

## Attention
_
