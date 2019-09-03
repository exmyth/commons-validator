package com.exmyth.commons.validator.message;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author exmyth
 * @date 2019-07-17 09:52
 * @description
 */
public interface ResourceBundleLocator {
    /**
     * Returns a resource bundle for the given locale.
     *
     * @param locale A locale, for which a resource bundle shall be retrieved. Must
     * not be null.
     *
     * @return A resource bundle for the given locale. May be null, if no such
     *         bundle exists.
     */
    ResourceBundle getResourceBundle(Locale locale);
}
