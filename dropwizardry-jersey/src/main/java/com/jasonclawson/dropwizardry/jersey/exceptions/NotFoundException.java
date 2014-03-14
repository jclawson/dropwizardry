package com.jasonclawson.dropwizardry.jersey.exceptions;

import javax.ws.rs.core.Response.Status;

import com.jasonclawson.dropwizardry.jersey.errors.BaseWebApplicationException;


public class NotFoundException extends BaseWebApplicationException {
    private static final long serialVersionUID = 1L;

    /**
     * Data will be included in the response 
     * 
     * @param message
     * @param data
     */
    public NotFoundException(String message, Object data) {
        super(message, data, Status.NOT_FOUND);
    }
    
    public NotFoundException(String message) {
        super(message, Status.NOT_FOUND);
    }
    
}
