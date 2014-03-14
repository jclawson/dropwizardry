package com.jasonclawson.dropwizardry.guice;

import io.dropwizard.Bundle;
import io.dropwizard.lifecycle.Managed;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.codahale.metrics.health.HealthCheck;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;

/**
 * Extend this guice module to get access to come dropwizard configurations like:
 *  - add healthchecks
 *  - register jersey resources
 *  - add lifecycle managed classes
 *  
 * @author jclawson
 *
 */
public abstract class AbstractDropwizardModule extends AbstractModule {
    private final Set<Class<? extends Bundle>> bundles = Sets.newHashSet();
    private final Set<Class<?>> jerseyResources = Sets.newHashSet();
    private final Set<Class<? extends Managed>> managed = Sets.newHashSet();
    private final Map<String, Class<? extends HealthCheck>> healthChecks = Maps.newHashMap();
    
    private GuiceJerseyEnvironment jerseyEnvironment;
    private GuiceLifecycleEnvironment lifecycleEnvironment;
    private HealthCheckEnvironment healthCheckEnvironment;
    
    /**
     * Anything added here will only have their run() method called. initialize() will not be called
     * @param bundleClass
     */
    protected void addBundle(Class<? extends Bundle> bundleClass) {
        bundles.add(bundleClass);
    }
    
    protected GuiceJerseyEnvironment jersey() {
        if(jerseyEnvironment == null) {
            jerseyEnvironment = new GuiceJerseyEnvironment();
        }
        return jerseyEnvironment;
    }
    
    protected GuiceLifecycleEnvironment lifecycle() {
        if(lifecycleEnvironment == null) {
            lifecycleEnvironment = new GuiceLifecycleEnvironment();
        }
        return lifecycleEnvironment;
    }
    
    protected HealthCheckEnvironment healthChecks() {
        if(healthCheckEnvironment == null) {
            healthCheckEnvironment = new HealthCheckEnvironment();
        }
        return healthCheckEnvironment;
    }
    

    @Override
    protected final void configure() {       
        configureModule();
        
        //bind dropwizard bundles
        Multibinder<Bundle> engineBinder = 
                Multibinder.newSetBinder(binder(), Bundle.class, Names.named("dw-bundles"));
        for(Class<? extends Bundle> bundle : bundles) {
            engineBinder.addBinding().to(bundle);
        }
        
        //bind jersey resources
        Multibinder<Object> jerseyBinder = 
                Multibinder.newSetBinder(binder(), Object.class, Names.named("dw-jersey-resources"));
        for(Class<?> resource : jerseyResources) {
            jerseyBinder.addBinding().to(resource);
        }
        
        //bind managed
        Multibinder<Managed> managedBinder = 
                Multibinder.newSetBinder(binder(), Managed.class, Names.named("dw-managed"));
        for(Class<? extends Managed> m : managed) {
            managedBinder.addBinding().to(m);
        }
        
        //bind healthchecks
        MapBinder<String, HealthCheck> healthCheckBinder = MapBinder.newMapBinder(binder(), String.class, HealthCheck.class, Names.named("dw-healthchecks"));
        for(Entry<String, Class<? extends HealthCheck>> entry : this.healthChecks.entrySet()) {
            healthCheckBinder.addBinding(entry.getKey()).to(entry.getValue());
        }
    }
    
    protected abstract void configureModule();
    
    
    public class GuiceJerseyEnvironment {
        public GuiceJerseyEnvironment register(Class<?> componentClass) {
            jerseyResources.add(componentClass);
            return this;
        }
    }

    public class GuiceLifecycleEnvironment {
        public GuiceLifecycleEnvironment manage(Class<? extends Managed> managedClass) {
            managed.add(managedClass);
            return this;
        }
    }
    
    public class HealthCheckEnvironment {
        public HealthCheckEnvironment register(String name, Class<? extends HealthCheck> healthCheck) {
            healthChecks.put(name, healthCheck);
            return this;
        }
    }
    
//TODO add servlets    
//    public class GuiceServletsEnvironment {
//        public GuiceServletsEnvironment register(Class<?> componentClass) {
//            jerseyResources.add(componentClass);
//            return this;
//        }
//    }
}
