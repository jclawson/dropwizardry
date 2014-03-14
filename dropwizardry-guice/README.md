Dropwizardry Guice!
===
Yet another library for Guice support within dropwizard.

Why this one is better
--
No autoconfig. Autoconfig in other libraries has caused issues with trying to do certain things in guice, so we removed it-- for now at least.

The guice injector is created during the run phase. Other libraries create the injector during the initialize phase but this creates a chicken and the egg scenario when you want to inject the most important object in the whole application--- the configuration object! The egg came first.

Configure dropwizard, the guice way. You don't need to add things to the environment anymore (but you still can if you want). This library provides an AbstractDropwizardModule that allows you to bind resources, manage instance lifecycles, add healthchecks... and more... all from within a guice module at bind time.


Credits
---
Some code was taken from the hubspot dropwizard guice library. https://github.com/HubSpot/dropwizard-guice. Attribution is provided in the code.