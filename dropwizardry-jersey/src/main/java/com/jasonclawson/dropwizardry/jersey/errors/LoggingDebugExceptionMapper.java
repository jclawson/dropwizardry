package com.jasonclawson.dropwizardry.jersey.errors;



import io.dropwizard.jersey.errors.ErrorMessage;

import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
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
    
    @Override
    public Response toResponse(E exception) {
        if (exception instanceof WebApplicationException) {
            return ((WebApplicationException) exception).getResponse();
        }

        final long id = ThreadLocalRandom.current().nextLong();
        logException(id, exception);
        
        if(debuggable && request.getHeader("X-Debug") != null && request.getHeader("X-Debug").equalsIgnoreCase("true")) {
            return Response.serverError()
                    .entity(new DebugErrorMessage(id, exception))
                    .build();
        } else {
            return Response.serverError()
                    .entity(new ErrorMessage(formatErrorMessage(id, exception)))
                    .build();
        }
    }
    protected String formatErrorMessage(long id, E exception) {
        return String.format("There was an error processing your request. It has been logged (ID %016x).", id);
    }

    protected void logException(long id, E exception) {
        LOGGER.error(formatLogMessage(id, exception), exception);
    }
    protected String formatLogMessage(long id, Throwable exception) {
        return String.format("Error handling a request: %016x", id);
    }
}
