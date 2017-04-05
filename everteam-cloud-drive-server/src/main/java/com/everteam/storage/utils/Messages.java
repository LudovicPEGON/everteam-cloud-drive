package com.everteam.storage.utils;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

/**
 * Helper to simplify accessing i18n messages in code.
 * 
 * This finds messages automatically found from src/main/resources/i18n (files named messages_*.properties)
 * 
 */
@Component
public class Messages {

    @Autowired
    private MessageSource messageSource;

    private MessageSourceAccessor accessor;

    @PostConstruct
    private void init() {
        accessor = new MessageSourceAccessor(messageSource, LocaleContextHolder.getLocale());
    }

    public String get(String code) {
        return accessor.getMessage(code);
    }
    
    public String get(String code, Object... args ) {
        return accessor.getMessage(code, args);
    }

}