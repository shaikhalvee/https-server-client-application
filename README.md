# https-server-client-application
Spring Boot Server &amp; Client Application

## Findings
If application.properties file containes ssl values{key, pass, keystore-path} client api must be called with https,
If there is no ssl values in application.properties file, client api can be called with http. 
But when client is in http mode, the https server certificate file must be imported into $JAVA_HOME/lib/security/cacerts file, 
otherwise client will not be able to connect with https server.
