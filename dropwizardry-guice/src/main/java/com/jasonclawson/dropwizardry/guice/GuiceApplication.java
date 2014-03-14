package com.jasonclawson.dropwizardry.guice;

import com.jasonclawson.dropwizardry.guice.support.GuiceBootstrapModule;
import com.jasonclawson.dropwizardry.guice.support.GuiceSupport;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.dropwizard.util.Generics;

/**
 * Extend this class instead of <code>Application</code> in order to get guice support.
 * 
 * You will be required to implement the method <code>configure(T, Builder<T>)</code>. Add
 * your Guice modules to the Builder. <code>configure</code> is analgous to <code>run</code>.
 * 
 * @author jclawson
 *
 * @param <T>
 */
public abstract class GuiceApplication<T extends Configuration> extends Application<T> {
    @Override
    public final void run(T configuration, Environment environment) throws Exception {
        GuiceSupport.Builder<T> guiceBuilder = GuiceSupport.newBuilder();
        configure(configuration, guiceBuilder);
        @SuppressWarnings("unchecked")
        Class<T> configurationClass = (Class<T>) Generics.getTypeParameter(this.getClass());
        
        
        //this will initialize any registered bundles
        guiceBuilder.addModule(new GuiceBootstrapModule());
        
        GuiceSupport<T> guiceBundle = guiceBuilder
                .setConfigClass(configurationClass)
                .build();
        
        guiceBundle.run(configuration, environment);
        
        //intialize the injector
        guiceBundle.getInjector();
    }
    
    
    
    public abstract void configure(T configuration, com.jasonclawson.dropwizardry.guice.support.GuiceSupport.Builder<T> guiceBuilder);
}
