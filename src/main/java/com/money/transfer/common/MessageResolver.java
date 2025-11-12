package com.money.transfer.common;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageResolver {

    private final MessageSource messageSource;

    public String getExceptionMessage(final String exceptionCode, final String... args) {
        return messageSource.getMessage(exceptionCode, args, LocaleContextHolder.getLocale());
    }

    public String getExceptionMessage(final String exceptionCode) {
        return messageSource.getMessage(exceptionCode, null, LocaleContextHolder.getLocale());
    }
}
