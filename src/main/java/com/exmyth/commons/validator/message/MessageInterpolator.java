package com.exmyth.commons.validator.message;

import java.util.Locale;
import java.util.Map;

/**
 * @author exmyth
 * @date 2019-07-17 10:10
 *
 */
public interface MessageInterpolator {

    String interpolate(String messageTemplate, Context context);


    String interpolate(String messageTemplate, Context context,  Locale locale);

    interface Context {
        /**
         * Returns the currently validated root bean type.
         *
         * @return The currently validated root bean type.
         */
        Class<?> getRootBeanType();

        /**
         * Returns the field.
         * @return
         */
        String[] getFieldNames();

        /**
         * @return the message parameters added to this context for interpolation
         *
         * @since 5.4.1
         */
        Map<String, Object> getMessageParameters();

        /**
         * @return value being validated
         */
        Object getValidatedValue();
    }
}
