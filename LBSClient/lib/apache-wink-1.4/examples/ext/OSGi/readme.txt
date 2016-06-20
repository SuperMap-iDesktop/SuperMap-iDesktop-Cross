OsgiHelloWorld Example
===============================================================================

Description 
===============================================================================
- This example demonstrate the most basic implementation of a an osgi bundle
  providing a root resource as a service
- It requires an OSGi container with the wink-osgi bundle installed and running
- Running "mvn pax:run" will run the felix osgi container with the necessary dependencies
- If the OSGi HTTP server is configured to run on port 8080 a greeting page
  will be available at http://localhost:8080/demo/jaxrs 
- Another one at http://localhost:8080/demo/jaxrs/greeting, this latter is 
  rendered with a MessageBodyWriter for the returned greeting object.


Build
===============================================================================
- see build instructions in examples/readme.txt file

--- readme.txt EOF ---
