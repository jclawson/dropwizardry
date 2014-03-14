package com.jasonclawson.dropwizardry.jersey.exceptions;

import javax.ws.rs.core.Response.Status;

import com.jasonclawson.dropwizardry.jersey.errors.BaseWebApplicationException;


public class InternalServerErrorException extends BaseWebApplicationException {
    private static final long serialVersionUID = 1L;

    public InternalServerErrorException(String message) {
        super(message, Status.INTERNAL_SERVER_ERROR);
    }
    
}
