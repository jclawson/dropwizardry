package com.jasonclawson.dropwizardry.jersey.errors;

import io.dropwizard.jersey.errors.ErrorMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.google.common.base.Charsets;

@Provider
@Consumes(MediaType.WILDCARD)
@Produces(MediaType.TEXT_PLAIN)
public class ErrorMessagePlainMessageBodyWriter implements MessageBodyWriter<ErrorMessage> {

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == ErrorMessage.class;
    }

    @Override
    public long getSize(ErrorMessage t, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType) {
        return t.getMessage().length();
    }

    @Override
    public void writeTo(ErrorMessage t, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, final OutputStream entityStream)
            throws IOException, WebApplicationException {
        entityStream.write(t.getMessage().getBytes(Charsets.UTF_8));
    }
    
    

    
}
