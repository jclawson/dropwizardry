package com.jasonclawson.dropwizardry.jersey.errors;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import lombok.Getter;

import com.google.common.base.Optional;

/**
 * we need to be able to have a generic error bean which has exception class, message, stack
 * and be able to hold some kind of "data" which could be a list of errors, or other kind of failure
 * 
 * @author jclawson
 */
@Getter
public abstract class BaseWebApplicationException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final Status status;
    private final Optional<Object> data;
    
    public BaseWebApplicationException(String message, Status status) {
        this(message, status, (Throwable) null);
    }
    
    public BaseWebApplicationException(String message, Status status, Throwable cause) {
        this(message, null, status, cause);
    }
    
    public BaseWebApplicationException(String message, Object data, Status status) {
        this(message, data, status, null);
    }
    
    public BaseWebApplicationException(String message, Object data, Status status, Throwable cause) {
        super(message, cause);
        this.data = Optional.fromNullable(data);
        this.status = status;
    }
    
    protected ResponseBuilder createResponseBuilder() {
        return Response.status(status)
                       .entity(getMessage());
    }
}
