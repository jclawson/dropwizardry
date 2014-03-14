package com.jasonclawson.dropwizardry.guice;

import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;

/**
 * Extend this for defining your guice enabled dropwizard bundles 
 * and adding them via AbstractDropwizardModule. This makes it clear
 * guice enabled bundles only work with the run method. You can implement
 * Bundle instead, but initialize will never be called.
 * 
 * @author jclawson
 *
 */
public abstract class RuntimeBundle implements Bundle {
    
    @Override
    public final void initialize(Bootstrap<?> bootstrap) {}
    
}
