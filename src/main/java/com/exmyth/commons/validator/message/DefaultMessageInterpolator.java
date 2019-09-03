package com.exmyth.commons.validator.message;

import com.exmyth.commons.validator.util.ValidatorUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author exmyth
 * @date 2019-07-17 12:00
 * @description
 */
public final class DefaultMessageInterpolator implements MessageInterpolator {
    private final boolean cachingEnabled;
    public static final char LEFT_BRACE = '{';
    public static final char RIGHT_BRACE = '}';
    private final ConcurrentHashMap<LocalizedMessage, String> resolvedMessageMap;
    private static final DefaultMessageInterpolator instance = new DefaultMessageInterpolator();
    private static final ResourceBundleLocator resourceBundleLocator = new DefaultResourceBundleLocator();

    private DefaultMessageInterpolator() {
        this.cachingEnabled = true;
        this.resolvedMessageMap = new ConcurrentHashMap<>();
    }

    public static DefaultMessageInterpolator getInstance() {
        return instance;
    }

    @Override
    public String interpolate(String key, Context context) {
        return interpolate(key, context, Locale.getDefault());
    }

    @Override
    public String interpolate(String key, Context context, Locale locale) {
        String interpolatedMessage = interpolateMessage( key, context, locale );
        return interpolatedMessage;
    }

    private String interpolateMessage(String key, Context context, Locale locale){
        String message;

        if (cachingEnabled ) {
            message = resolvedMessageMap.computeIfAbsent( new LocalizedMessage( key, locale ),
                    lm -> resolveMessage( key, locale ) );
        } else {
            message = resolveMessage( key, locale );
        }

        if (message.indexOf(LEFT_BRACE) > -1 ) {
            message = resolveParameter(message, context);
        }
        return message;
    }

    private String resolveParameter(String message, Context context) {
        Map<String, Object> paramMap = context == null ? null : context.getMessageParameters();
        if(paramMap == null || paramMap.isEmpty()){
            return message;
        }
        String[] searchList = new String[paramMap.size()];
        String[] replacementList = new String[paramMap.size()];
        int i = 0;
        Iterator<Map.Entry<String, Object>> iter = paramMap.entrySet().iterator();
        while (iter.hasNext()){
            Map.Entry<String, Object> entry = iter.next();
            searchList[i] = addCurlyBraces(entry.getKey());
            if(entry.getValue().getClass().isArray()){
                replacementList[i] = Arrays.toString((String[])entry.getValue());
            } else {
                replacementList[i] = String.valueOf(entry.getValue());
            }
            i++;
        }
        String result = ValidatorUtil.replaceEach(message, searchList, replacementList);
        return result;
    }

    private String addCurlyBraces(String key) {
        return LEFT_BRACE + key + RIGHT_BRACE;
    }

    private String removeCurlyBraces(String message) {
        return message.substring( 1, message.length() - 1 );
    }

    private String resolveMessage(String key, Locale locale) {
        try {
            ResourceBundle resourceBundle = resourceBundleLocator.getResourceBundle(locale);
            String resolvedMessage = resourceBundle.getString(removeCurlyBraces(key));
            return resolvedMessage;
        } catch (MissingResourceException e) {
            return key;
        }
    }
}
