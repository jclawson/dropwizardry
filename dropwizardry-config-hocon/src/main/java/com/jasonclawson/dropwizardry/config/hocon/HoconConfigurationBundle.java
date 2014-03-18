package com.jasonclawson.dropwizardry.config.hocon;

import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Install this dropwizard bundle to use HOCON instead of the default YAML configuration parser
 * @author jclawson
 *
 */
public class HoconConfigurationBundle implements Bundle {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void initialize(Bootstrap<?> bootstrap) {
		bootstrap.setConfigurationFactoryFactory(new HoconConfigurationFactoryFactory());
	}

	public void run(Environment environment) {
		
	}

}
