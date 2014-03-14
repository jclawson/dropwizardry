package com.jasonclawson.dropwizardry.jersey.exceptions;

import javax.ws.rs.core.Response.Status;

import com.jasonclawson.dropwizardry.jersey.errors.BaseWebApplicationException;


public class BadRequestException extends BaseWebApplicationException {
    private static final long serialVersionUID = 1L;

    /**
     * Data will be included in the response 
     * 
     * @param message
     * @param data
     */
    public BadRequestException(String message, Object data) {
        super(message, data, Status.BAD_REQUEST);
    }
    
    public BadRequestException(String message) {
        super(message, Status.BAD_REQUEST);
    }
}
