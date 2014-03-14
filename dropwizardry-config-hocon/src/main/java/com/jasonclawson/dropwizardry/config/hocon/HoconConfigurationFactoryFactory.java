package com.jasonclawson.dropwizardry.config.hocon;

import javax.validation.Validator;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.dropwizard.configuration.ConfigurationFactory;
import io.dropwizard.configuration.ConfigurationFactoryFactory;

public class HoconConfigurationFactoryFactory<T> implements ConfigurationFactoryFactory<T> {
	@Override
    public ConfigurationFactory<T> create(
            Class<T>     klass,
            Validator    validator, 
            ObjectMapper objectMapper,
            String       propertyPrefix) {
        return new ConfigurationFactory<>(klass, validator, objectMapper, propertyPrefix);
    }
}
