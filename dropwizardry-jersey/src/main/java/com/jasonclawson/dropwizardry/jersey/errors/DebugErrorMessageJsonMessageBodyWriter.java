package com.jasonclawson.dropwizardry.jersey.errors;

import io.dropwizard.jersey.errors.ErrorMessage;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

@Provider
@Consumes(MediaType.WILDCARD)
@Produces(MediaType.APPLICATION_JSON)
public class DebugErrorMessageJsonMessageBodyWriter extends JacksonJsonProvider {
    public DebugErrorMessageJsonMessageBodyWriter(ObjectMapper mapper) {
        super(mapper);
    }
    
    
    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == DebugErrorMessage.class || type == ErrorMessage.class;
    }
}
