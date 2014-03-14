package com.jasonclawson.dropwizardry.jersey.errors;



import io.dropwizard.jersey.errors.ErrorMessage;

import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public abstract class LoggingDebugExceptionMapper<E extends Throwable> implements ExceptionMapper<E> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingDebugExceptionMapper.class);
    private final boolean debuggable;
    
    public LoggingDebugExceptionMapper(boolean debuggable) {
        this.debuggable = debuggable;
    }
    
    /**
     * This will be injected as a proxy of the request scoped servlet request
     */
    @Context HttpServletRequest request;
    
    private boolean isDebuggable() {
        String debugHeader = request.getHeader("X-Debug");
        return this.debuggable && "true".equalsIgnoreCase(debugHeader);
    }
    
    @Override
    public Response toResponse(E exception) {
        if (exception instanceof WebApplicationException) {
            return ((WebApplicationException) exception).getResponse();
        }

        boolean debuggable = isDebuggable();
        final long id = ThreadLocalRandom.current().nextLong();
        
        /*
         * If we are a BaseWebApplicationException then we will use its data to build a 
         * response.
         */
        if(exception instanceof BaseWebApplicationException) {
            ResponseBuilder builder = ((BaseWebApplicationException) exception).createResponseBuilder();
            if(!debuggable) {
                return builder.entity(new ErrorMessage(exception.getMessage()))
                              .build();
            } else {
                builder.entity(new DebugErrorMessage(id, exception));
                debugLogException(id, exception);
                return builder.build();
            }
        }
        
        //here we have an unexpected exception of some kind (its not a BaseWebApplicationException)
        //we must sanitize the exception message we send back to the client 
        //unless its debuggable
        
        logException(id, exception);
        
        if(debuggable) {
            return Response.serverError()
                    .entity(new DebugErrorMessage(id, exception))
                    .build();
        } else {
            return Response.serverError()
                    .entity(new ErrorMessage(formatErrorMessage(id)))
                    .build();
        }
    }
    protected static String formatErrorMessage(long id) {
        return String.format("There was an error processing your request. It has been logged (ID %016x).", id);
    }

    protected void logException(long id, E exception) {
        LOGGER.error(formatLogMessage(id, exception), exception);
    }
    
    protected void debugLogException(long id, E exception) {
        LOGGER.debug(formatLogMessage(id, exception), exception);
    }
    
    protected static String formatLogMessage(long id, Throwable exception) {
        return String.format("Error handling a request: %016x", id);
    }
}
