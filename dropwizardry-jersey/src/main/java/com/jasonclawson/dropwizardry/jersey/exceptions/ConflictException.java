package com.jasonclawson.dropwizardry.jersey.exceptions;

import javax.ws.rs.core.Response.Status;

import com.jasonclawson.dropwizardry.jersey.errors.BaseWebApplicationException;


public class ConflictException extends BaseWebApplicationException {
    private static final long serialVersionUID = 1L;

    /**
     * Data will be included in the response 
     * 
     * @param message
     * @param data
     */
    public ConflictException(String message, Object data) {
        super(message, data, Status.CONFLICT);
    }
    
    public ConflictException(String message) {
        super(message, Status.CONFLICT);
    }
}
