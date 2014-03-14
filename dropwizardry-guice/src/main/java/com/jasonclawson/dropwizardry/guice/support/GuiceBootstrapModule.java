package com.jasonclawson.dropwizardry.guice.support;

import io.dropwizard.Bundle;
import io.dropwizard.Configuration;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Environment;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.validation.Validator;

import lombok.extern.slf4j.Slf4j;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.jasonclawson.dropwizardry.guice.AbstractDropwizardModule;

/**
 * Initializes dropwizard bundles that were added with AbstractDropwizardModule. These 
 * bundles are instances of Bundle, not ConfiguredBundle because you can simply inject 
 * the Configuration in.
 * 
 * @author jclawson
 */
@Slf4j
public class GuiceBootstrapModule extends AbstractDropwizardModule {
    @Override
    protected void configureModule() {
        bind(GuiceDropwizardBundleInitializer.class).asEagerSingleton();
    }
    
    @Provides
    protected MetricRegistry provideMetricRegistry(Environment environment) {
        return environment.metrics();
    }
    
    @Provides
    protected Validator provideValidator(Environment environment) {
        return environment.getValidator();
    }
    
    protected static class GuiceDropwizardBundleInitializer<T extends Configuration>{
        @Inject
        protected GuiceDropwizardBundleInitializer(
                @Named("dw-bundles") Set<Bundle> bundles,
                @Named("dw-jersey-resources") Set<Object> resources,
                @Named("dw-managed") Set<Managed> managed,
                @Named("dw-healthchecks") Map<String, HealthCheck> healthChecks,
                Environment environment) {
            
            for(Bundle bundle : bundles) {
                bundle.run(environment);
            }
            
            //TODO: how does this play with dropwizard?
            for(Object resource : resources) {
                log.info("Adding "+resource.getClass().getSimpleName()+" to Jersey");
                environment.jersey().register(resource);
            }
            
            for(Managed m : managed) {
                log.info("Adding "+m.getClass().getSimpleName()+" to lifecycle management");
                environment.lifecycle().manage(m);
            }
            
            for(Entry<String, HealthCheck> h : healthChecks.entrySet()) {
                log.info("Adding "+h.getKey()+":"+h.getValue().getClass().getSimpleName()+" to health checks");
                environment.healthChecks().register(h.getKey(), h.getValue());
            }
        }
    }
}
