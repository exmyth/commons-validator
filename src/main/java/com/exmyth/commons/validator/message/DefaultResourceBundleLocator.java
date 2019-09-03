package com.exmyth.commons.validator.message;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author exmyth
 * @date 2019-07-17 09:58
 * @description
 */
public final class DefaultResourceBundleLocator implements ResourceBundleLocator {
    private static final String PROPERTIES_FILE_NAME = "ValidationMessages";
    private Map<Locale, ResourceBundle> bundleCache = new ConcurrentHashMap<>();

    @Override
    public ResourceBundle getResourceBundle(Locale locale) {
        ResourceBundle bundle = bundleCache.computeIfAbsent(locale,
                lo -> ResourceBundle.getBundle(PROPERTIES_FILE_NAME, lo, getClass().getClassLoader()));
        return bundle;
    }
}
