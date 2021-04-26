# Demo-iDAAS-Connect-EDI
Demonstration of iDAAS EDI Capabilities


The intent of these artifacts to enable
resources to work locally: <br/>
+ platform-scripts: support running kafka, creating/listing and deleting topics needed for this solution
   and also building and packaging the solution as well. All the scripts are named to describe their capabilities <br/>
+ platform-testdata: sample transactions to leverage for using the platform. <br/>

# Pre-Setup (Before Running Demo)
For this demonstration we have done the following steps.

1.  This is a basic demo that picks up EDI files from the file system. You should not need to create anything as the files are 
    checked in to have the directory structure laid out. 

We would expect that since these are all parameters within the application.properties file you can change these to your needs and liking.

## Scenario: EDI Data Processing
This repository follows a very common general implementation of processing a file from a filesystem. The intent is to pick
up the file and process it and then leverage the existing iDaaS-EventBuilder library to show it being processed and manipulated.

### Integration Data Flow Steps

1. Every 1 minute the defined directory is looked at for any .edi file, if found the file is processed into a matching structure.
2. The data structure is then persisted into a kafka topic.

# Start The Engine!!!
This section covers the running of the solution. There are several options to start the Engine Up!!!

## Step 1: Kafka Server To Connect To
In order for ANY processing to occur you must have a Kafka server running that this accelerator is configured to connect to.
Please see the following files we have included to try and help: <br/>
[Kafka](https://github.com/RedHat-Healthcare/iDaaS-Demos/blob/master/Kafka.md)<br/>
[KafkaWindows](https://github.com/RedHat-Healthcare/iDaaS-Demos/blob/master/KafkaWindows.md)<br/>

## Step 2: Running the App: Maven or Code Editor
This section covers how to get the application started.
+ Maven: go to the directory of where you have this code. Specifically, you want to be at the same level as the POM.xml file and execute the
following command: <br/>
```
mvn clean install
 ```
Depending upon if you have every run this code before and what libraries you have already in your local Maven instance it could take a few minutes.
+ Code Editor: You can right click on the Application.java in the /src/<application namespace> and select Run
### Design Pattern/Accelerator Configuration
All iDaaS Design Pattern/Accelelrators have application.properties files to enable some level of reusability of code and simplfying configurational enhancements.<br/>
In order to run multiple iDaaS integration applications we had to ensure the internal http ports that
the application uses. In order to do this we MUST set the server.port property otherwise it defaults to port 8080 and ANY additional
components will fail to start. iDaaS Connect HL7 uses 9980. You can change this, but you will have to ensure other applications are not
using the port you specify.

## Ongoing Enhancements
We maintain all enhancements within the Git Hub portal under the
<a href="https://github.com/RedHat-Healthcare/iDAAS-Connect-ThirdParty/projects" target="_blank">projects tab</a>

## Defects/Bugs
All defects or bugs should be submitted through the Git Hub Portal under the
<a href="https://github.com/RedHat-Healthcare/iDAAS-Connect-ThirdPartyt/issues" target="_blank">issues tab</a>

## Chat and Collaboration
You can always leverage <a href="https://redhathealthcare.zulipchat.com" target="_blank">Red Hat Healthcare's ZuilpChat area</a>
and find all the specific areas for iDAAS-Connect-ThirdParty. We look forward to any feedback!!

If you would like to contribute feel free to, contributions are always welcome!!!!

Happy using and coding....

