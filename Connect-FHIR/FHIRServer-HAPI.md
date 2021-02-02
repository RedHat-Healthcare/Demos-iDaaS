# HAPI FHIR Server
No way is this intended to be an install or configuration guide. So we are trying to just convey 
any specifics we have learned or how we leverage the technologies. This ONLY deals with the freely available
open source offering. If using Smile-CDR then you will just need to enhance the configuration to match the 
defined server.

# Pre-Requisites
For this you can use your favorite mechanism to build Java code. 

## JDK
As for JDK specifics we have several on machines but typically leverage OpenJDK8 for these builds.

## Maven
You will need to be able to leverage Maven locally.

# Source Code
For this implementation you can start by downloading the code <a href="https://github.com/hapifhir/hapi-fhir-jpaserver-starter" target="_blank">
by starting here</a>

1. Download the source code
2. From your command line or IDE (where the POM.xml file is located) go ahead and do a build.

# Running the Built Solution
We have taken these instructions from various online forums and learning. 

1.  From the POM.xml file location run: mvn -Djetty.port=8888 jetty:run (this will start the Jetty server on port 8888).
if you want to change the port just change the number after port=.

# Seeing if Solution Running

1. Follow the Running the Built Solution Steps. 
2. You can then go to http://hostname:portnumber/hapi-fhir-jpaserver/fhir/

# To Change any configuration within iDAAS

1. Go into the respective application.properties file and make sure that the setting: 
idaas.hapiURI=http://localhost:8888/hapi-fhir-jpaserver/fhir/ 
is changed to whatever port or hostname the server runs on.
2. Restart the application for the setting to be implemented

Enjoy and Happy Coding!!!!
