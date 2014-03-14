package com.jasonclawson.dropwizardry.guice.support;


import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.List;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.spi.container.servlet.ServletContainer;


/**
 * 
 * Source taken from https://github.com/HubSpot/dropwizard-guice
 * License: Apache License Version 2.0
 * @author eliast
 * @author jclawson
 * 
 * This source was modified to get rid of autoconfig and make bindings happen
 * during the dropwizard run phase instead of initialize phase
 *
 * @param <T>
 */
public class GuiceSupport<T extends Configuration> implements ConfiguredBundle<T> {

    final Logger logger = LoggerFactory.getLogger(GuiceSupport.class);

    private final List<Module> modules;
    private Injector injector;
    
    /**
     * Supressing rawtypes here because this may not be T if the user didn't specify the generics
     */
    @SuppressWarnings("rawtypes")
    private DropwizardEnvironmentModule dropwizardEnvironmentModule;
    
    private Optional<Class<T>> configurationClass;
    private GuiceContainer container;
    private Stage stage;


    public static class Builder<T extends Configuration> {
        private List<Module> modules = Lists.newArrayList();
        private Optional<Class<T>> configurationClass = Optional.<Class<T>>absent();

        public Builder<T> addModule(Module module) {
            Preconditions.checkNotNull(module);
            modules.add(module);
            return this;
        }

        public Builder<T> setConfigClass(Class<T> clazz) {
            configurationClass = Optional.of(clazz);
            return this;
        }

        public GuiceSupport<T> build() {
            return build(Stage.PRODUCTION);
        }

        public GuiceSupport<T> build(Stage s) {
            return new GuiceSupport<T>(s, modules, configurationClass);
        }

    }

    public static <T extends Configuration> Builder<T> newBuilder() {
        return new Builder<T>();
    }

    private GuiceSupport(Stage stage, List<Module> modules, Optional<Class<T>> configurationClass) {
        Preconditions.checkNotNull(modules);
        Preconditions.checkArgument(!modules.isEmpty());
        Preconditions.checkNotNull(stage);
        this.modules = modules;
        this.configurationClass = configurationClass;
        this.stage = stage;
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        
    }

    private void initInjector() {
        injector = Guice.createInjector(this.stage, modules);
    }

    @Override
    public void run(final T configuration, final Environment environment) {
        if (configurationClass.isPresent()) {
            dropwizardEnvironmentModule = new DropwizardEnvironmentModule<T>(configurationClass.get());
        } else {
            dropwizardEnvironmentModule = new DropwizardEnvironmentModule<Configuration>(Configuration.class);
        }
        
        setEnvironment(configuration, environment);
        
        container = new GuiceContainer();
        JerseyContainerModule jerseyContainerModule = new JerseyContainerModule(container);
        
        modules.add(jerseyContainerModule);
        modules.add(dropwizardEnvironmentModule);

        initInjector();
        
        container.setResourceConfig(environment.jersey().getResourceConfig());
        environment.jersey().replace(new Function<ResourceConfig, ServletContainer>() {
            @Nullable
            @Override
            public ServletContainer apply(ResourceConfig resourceConfig) {
                return container;
            }
        });
        environment.servlets().addFilter("Guice Filter", GuiceFilter.class)
                .addMappingForUrlPatterns(null, false, environment.getApplicationContext().getContextPath() + "*");
        
    }

    @SuppressWarnings("unchecked")
    private void setEnvironment(final T configuration, final Environment environment) {
        dropwizardEnvironmentModule.setEnvironmentData(configuration, environment);
    }

    public Injector getInjector() {
        return injector;
    }
}

