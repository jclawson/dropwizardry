package com.jasonclawson.dropwizardry.jersey;

import io.dropwizard.ConfiguredBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jasonclawson.dropwizardry.jersey.errors.BaseWebApplicationException;
import com.jasonclawson.dropwizardry.jersey.errors.DebugErrorMessageJsonMessageBodyWriter;
import com.jasonclawson.dropwizardry.jersey.errors.DebugErrorMessagePlainMessageBodyWriter;
import com.jasonclawson.dropwizardry.jersey.errors.ErrorMessagePlainMessageBodyWriter;
import com.jasonclawson.dropwizardry.jersey.errors.LoggingDebugExceptionMapper;


@SuppressWarnings("rawtypes")
public class DropwizardryJerseyBundle implements ConfiguredBundle {

    private final ObjectMapper objectMapper;
    
    public DropwizardryJerseyBundle(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    @Override
    public void initialize(Bootstrap bootstrap) {}

    @Override
    public void run(Object configuration, Environment environment) {
        boolean debuggable = false;
        if(configuration instanceof JerseyDebuggable) {
            debuggable = ((JerseyDebuggable) configuration).isJerseyDebuggable();
        }
        
        JerseyEnvironment jersey = environment.jersey();
        jersey.register(new DebugErrorMessageJsonMessageBodyWriter(objectMapper));
        jersey.register(new DebugErrorMessagePlainMessageBodyWriter());
        jersey.register(new ErrorMessagePlainMessageBodyWriter());
        
        jersey.register(new LoggingDebugExceptionMapper<Throwable>(debuggable) {});
        jersey.register(new LoggingDebugExceptionMapper<BaseWebApplicationException>(debuggable) {});
        
        //enable returning validation errors
        jersey.enable("jersey.config.beanValidation.enableOutputValidationErrorEntity.server");
    }
    
}
