package com.jasonclawson.dropwizardry.guice.support;


import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import lombok.extern.slf4j.Slf4j;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

/**
 *
 * @param <T>
 */
@Slf4j
public class DropwizardEnvironmentModule<T extends Configuration> extends AbstractModule {
    private final T configuration;
    private final Environment environment;
    private Class<? super T> configurationClass;

    public DropwizardEnvironmentModule(Class<T> configurationClass, T configuration, Environment environment) {
        this.configurationClass = configurationClass;
        this.configuration = configuration;
        this.environment = environment;
    }

    @Override
    protected void configure() {
        bind(Configuration.class).toInstance(configuration);
        if(configurationClass != Configuration.class) {
            bind(configurationClass).toInstance(configuration);
        }
        bind(Environment.class).toInstance(environment);
        initConfigurationBindings();
    }
    
    @SuppressWarnings("unchecked")
    private void initConfigurationBindings() {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(configurationClass);
            PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
            
            Object current = configuration;
            for(PropertyDescriptor prop : props) {
                Method readMethod = prop.getReadMethod();
                if(readMethod.getParameterTypes().length == 0) {
                    try {
                        Object value = readMethod.invoke(current);
                        
                        //TODO: add support for binding generic types too!
                        if(value.getClass().getGenericInterfaces().length > 0) {
                            log.warn("Binding of generic types is not supported yet. Will not bind {}", value.getClass());
                            continue;
                        }
                        
                        bind((Class<Object>)value.getClass())
                            .annotatedWith(Names.named(prop.getName()))
                            .toInstance(value);
                        log.debug("Binding {} annotated with \"{}\"", value.getClass(), prop.getName());
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        log.error("Unable to bind '{}' due to readMethod invoke error",prop.getName(), e);
                    }
                    
                }
            }
            
        } catch (IntrospectionException e) {
            log.error("Unable to introspect bean info for {}. I will try to continue without binding configurations", configurationClass); 
        }
    }

}

