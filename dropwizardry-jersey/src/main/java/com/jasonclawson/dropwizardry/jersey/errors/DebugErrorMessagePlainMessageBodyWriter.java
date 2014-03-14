package com.jasonclawson.dropwizardry.jersey.errors;

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
public class DebugErrorMessagePlainMessageBodyWriter implements MessageBodyWriter<DebugErrorMessage> {

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == DebugErrorMessage.class;
    }

    @Override
    public long getSize(DebugErrorMessage t, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType) {
        return t.toString().length();
    }

    @Override
    public void writeTo(DebugErrorMessage t, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, final OutputStream entityStream)
            throws IOException, WebApplicationException {
        String value = t.toString();
        entityStream.write(value.getBytes(Charsets.UTF_8));
    }
    
    

    
}
