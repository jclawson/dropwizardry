package com.jasonclawson.dropwizardry.jersey.exceptions;

import javax.ws.rs.core.Response.Status;

import com.jasonclawson.dropwizardry.jersey.errors.BaseWebApplicationException;


public class UnauthorizedException extends BaseWebApplicationException {
    private static final long serialVersionUID = 1L;

    /**
     * Data will be included in the response 
     * 
     * @param message
     * @param data
     */
    public UnauthorizedException(String message, Object data) {
        super(message, data, Status.UNAUTHORIZED);
    }
    
    public UnauthorizedException(String message) {
        super(message, Status.UNAUTHORIZED);
    }
    
}
