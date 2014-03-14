Dropwizardry Jersey
===
This library includes some good Jersey defaults useful for any REST webservice. Just install the DropwizardryJerseyBundle!

Features
--
* Debug exception support. Make your configuration class implement `JerseyDebuggable` to enable using the `X-Debug` header. When enabled, and the `X-Debug` header is `'true'`, application level exceptions will include the full stack trace as JSON or plain/text.
* Included exceptions for many response status codes! Note: These exceptions don't extend `WebApplicationException` because I couldn't yet figure out how to change the serialization of these exceptions. Jersey seems to have something hardcoded in there.

TODO
--
I want to research more on how to handle error serialization better. The methodology employed here, while it works, isn't good enough. I want full control over all exception handling but Jersey seems to have hard coded some things in how it serializes `WebApplicationException`.