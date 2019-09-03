package com.exmyth.commons.validator.test;

import com.exmyth.commons.validator.constraint.field.NotBlank;
import com.exmyth.commons.validator.processor.inspect.Inspect;

/**
 * @author exmyth
 * @date 2019-08-25 14:42
 * @description
 */
public class RequestApi {
    @Inspect(field = "helloRequest.email", validator = NotBlank.class)
    @Inspect(field = "helloRequest.mobile", validator = NotBlank.class)
    @Inspect(field = "helloRequest.idNumber", validator = NotBlank.class)
    public void request(HelloRequest helloRequest){
    }

    static class HelloRequest{
        private String email;
        private String mobile;
        private String idNumber;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getIdNumber() {
            return idNumber;
        }

        public void setIdNumber(String idNumber) {
            this.idNumber = idNumber;
        }
    }
}
