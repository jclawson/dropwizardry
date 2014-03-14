package com.jasonclawson.dropwizardry.guice.support;


import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.ProvisionException;

/**
 * This code was taken from https://github.com/HubSpot/dropwizard-guice
 * 
 * License: Apache License Version 2.0
 * 
 * @author Mike Axiak
 * @author eliast
 * @author dmorgantini
 * @author lunacik
 *
 * @param <T>
 */
public class DropwizardEnvironmentModule<T extends Configuration> extends AbstractModule {
    private static final String ILLEGAL_DROPWIZARD_MODULE_STATE = "The dropwizard environment has not yet been set. This is likely caused by trying to access the dropwizard environment during the bootstrap phase.";
    private T configuration;
    private Environment environment;
    private Class<? super T> configurationClass;

    public DropwizardEnvironmentModule(Class<T> configurationClass) {
        this.configurationClass = configurationClass;
    }

    @Override
    protected void configure() {
        Provider<T> provider = new CustomConfigurationProvider();
        bind(configurationClass).toProvider(provider);
        if (configurationClass != Configuration.class) {
            bind(Configuration.class).toProvider(provider);
        }
    }

    public void setEnvironmentData(T configuration, Environment environment) {
        this.configuration = configuration;
        this.environment = environment;
    }

    @Provides
    public Environment providesEnvironment() {
        if (environment == null) {
            throw new ProvisionException(ILLEGAL_DROPWIZARD_MODULE_STATE);
        }
        return environment;
    }

    private class CustomConfigurationProvider implements Provider<T> {
        @Override
        public T get() {
            if (configuration == null) {
                throw new ProvisionException(ILLEGAL_DROPWIZARD_MODULE_STATE);
            }
            return configuration;
        }
    }
}

