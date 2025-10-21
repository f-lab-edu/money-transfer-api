package com.money.transfer.common;

import com.money.transfer.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public abstract class BaseControllerTest {

    protected MockMvc mockMvc;
    protected ResourceBundleMessageSource messageSource;
    protected LocalValidatorFactoryBean validator;

    @BeforeEach
    void setUpBase() {
        messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");

        validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(messageSource);

        mockMvc = MockMvcBuilders.standaloneSetup(getController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    protected abstract Object getController();
}
