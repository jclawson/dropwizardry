package com.jasonclawson.dropwizardry.config.hocon;

import io.dropwizard.configuration.ConfigurationFactory;
import io.dropwizard.configuration.ConfigurationFactoryFactory;
import io.dropwizard.configuration.HoconConfigurationFactory;

import javax.validation.Validator;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HoconConfigurationFactoryFactory<T> implements ConfigurationFactoryFactory<T> {
	@Override
    public ConfigurationFactory<T> create(
            Class<T>     klass,
            Validator    validator, 
            ObjectMapper objectMapper,
            String       propertyPrefix) {
        return new HoconConfigurationFactory<>(klass, validator, objectMapper, propertyPrefix);
    }
}
