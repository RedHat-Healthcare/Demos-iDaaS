# Demo-iDAAS-Connect-EDI
Demonstration of iDAAS EDI Capabilities


The intent of these artifacts to enable
resources to work locally: <br/>
1. platform-addons: needed software to run locally. This currently contains amq-streams-1.5 (which is the upstream of Kafka 2.5)<br/>
2. platform-scripts: support running kafka, creating/listing and deleting topics needed for this solution
   and also building and packaging the solution as well. All the scripts are named to describe their capabilities <br/>
3. platform-testdata: sample transactions to leverage for using the platform. <br/>
4. platform-ddl: The DDL for the database that is used.

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

## Builds
This section will cover both local and automated builds.

### Local Builds
Within the code base you can find the local build commands in the /platform-scripts directory
1.  Run the build-solution.sh script
    It will run the maven commands to build and then package up the solution. The package will use the usual settings
    in the pom.xml file. It pulls the version and concatenates the version to the output jar it builds.
    Additionally, there is a copy statement to remove any specific version, so it outputs idaas-connect-hl7.jar

### Automated Builds
Automated Builds are going to be done in Azure Pipelines

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

