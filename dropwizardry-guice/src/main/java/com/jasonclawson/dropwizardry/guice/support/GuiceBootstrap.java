package com.jasonclawson.dropwizardry.guice.support;

import io.dropwizard.Bundle;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.LinkedList;
import java.util.List;

import com.google.inject.Injector;

/**
 * 
 * 
 * @author jclawson
 *
 * @param <T>
 */
public class GuiceBootstrap<T extends Configuration> implements Bundle {

    private final Injector injector;
    private final List<Class<? extends Bundle>> runtimeBundleClasses = new LinkedList<>();
    private boolean isInitialized = false;
    
    public GuiceBootstrap(Injector injector) {
        this.injector = injector;
    }
    
    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        
    }

    @Override
    public void run(Environment environment) {
        for(Class<? extends Bundle> bundleClass : runtimeBundleClasses) {
            Bundle runtimeBundle = injector.getInstance(bundleClass);
            runtimeBundle.run(environment);
        }
        
        //free up memory
        runtimeBundleClasses.clear();
        isInitialized = true;
    }
    
    public void addBundle(Class<? extends Bundle> bundleClass) {
        if(isInitialized) {
            throw new IllegalStateException("You cannot add bundles after GuiceBootstrap has been run");
        }
        runtimeBundleClasses.add(bundleClass);
    }
}
