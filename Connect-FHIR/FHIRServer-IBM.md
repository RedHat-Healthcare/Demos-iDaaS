# IBM FHIR Server
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

# Downloading the Release
While you can download the source code you can also download the release.
For this implementation you can start by downloading the code <a href="https://github.com/IBM/FHIR/releases" target="_blank">
by starting here </a>and going down to Using the Release section.

1. Download the Release zip file
2. Follow the <a href="https://ibm.github.io/FHIR/guides/FHIRServerUsersGuide/#21-installing-a-new-server" target="_blank">
Documentation Link</a>. 
3. The ONLY thing we have done is move the directory of wlp which is underneath liberty-runtime that was created when we 
executed ./install.sh where the files were initial unzipped to the base platform directory. 
into to limit the directories and potential confusion. 
4. Modify the server.xml file as needed. <br/>
We have included the one we use in the platform-addons/IBMFHIRServer directory. We have defined the http port as 8090
and created the users: fhiruser with a password of FHIRDeveloper123 and fhiradmin with a password of fhiradmin.

# Running the Built Solution
We have taken these instructions from various online forums and learning. 

1.  Create a script or run from a command line/terminal window:
.<directory to wlp>/server start fhir-server.sh ot .bat

# Seeing if Solution Running
1. If you used the server.xml file we have included then go to http://localhost:8090/openapi/ui/

# To Change any configuration within iDAAS
1. Go into the respective application.properties file and make sure that the setting: 
idaas.ibmURI=http://localhost:8090/fhir-server/api/v4/ 
is changed to whatever port or hostname the server runs on.
2. Restart the application for the setting to be implemented

Enjoy and Happy Coding!!!!
