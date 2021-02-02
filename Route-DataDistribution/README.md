# Demo-iDAAS-Route-DataDistribution
iDAAS Route data distribution is part of the iDAAS Connect family within iDAAS. iDAAS Connect is the family of capabilities
that specifically address data connectivity and data distribution of information and iDaaS Route Data Distribution
is all about how you can distribute the data. Before data can be distributed it MUST enter into the iDAAS platform from one one of the iDAAS Connect family that help
organizations connect to data: HL7 (iDAAS-Connect-HL7), FHIR (iDAAS-Connect-FHIR), Third Party/Other (iDAAS-Connect-ThirdParty).

Why not implement this directly within the iDAAS Connect components that connect to data? You most certainly could; however, we
wanted to ensure with this accelerator platform we include a very specific capability to enable the separation of inbound data and
any routing activities. Our reason is to ensure that anything processing inbound data to a platform be able to focus on that
key need and not risk potential downtime for other related tasks.

This solution contains three supporting directories. The intent of these artifacts to enable
resources to work locally: <br/>
1. platform-scripts: support running amq, amq-streams (kafka) and doing very specific things with
   Kafka such as: creating/listing and deleting topics needed for this solution
   and also building and packaging the solution as well. All the scripts are named to describe their capabilities <br/>
2. platform-testdata: sample transactions to leverage for using the platform.

## Scenario: Integration
This repository follows a very common general facility based implementation. The implementation
is of a facility, we have named MCTN for an application we have named MMS. 

### Integration Data Flow Steps

* Any external connecting system has sent data, in this demo example HL7 and FHIR messages.
* The HL7 message will be moved from a single application based event by application and data type
   (MCTN_MMS_ADT) to three specific copies: an application level copy (MMS_ADT), a facility by data 
   type (MCTN_ADT), and an enterprise by data type (ENT_ADT).
* The FHIR Message will be moved from its topic (fhirsvr_adverseevent) to an enterprise copy (ent_fhirsvr_adverseevent).

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

## Running
In order to run multiple iDaaS integration applications we had to ensure the internal http ports that
the application uses. In order to do this we set the server.port property. We have tried to keep these internally
separate. iDaaS Connect HL7 uses 9980. You can change this, but you will have to ensure other applications are not
using the port you specify.

```properties
server.port=9980
```

Once built you can run the solution by executing `./platform-scripts/start-solution.sh`.
The script will startup Kafka and iDAAS server.

It is possible to overwrite configuration by:
1. Providing parameters via command line e.g.
   `./start-solution.sh --server.port=10009`
2. Creating an application.properties next to the idaas-route-datadistribution.jar in the target directory
3. Creating a properties file in a custom location `./start-solution.sh --spring.config.location=file:./config/application.properties`

# Getting Involved
Here are a few ways you can get or stay involved.

## Ongoing Enhancements
We maintain all enhancements within the Git Hub portal under the
<a href="https://github.com/RedHat-Healthcare/iDAAS-Connect-HL7/projects" target="_blank">projects tab</a>

## Defects/Bugs
All defects or bugs should be submitted through the Git Hub Portal under the
<a href="https://github.com/RedHat-Healthcare/iDAAS-Connect-HL7/issues" target="_blank">issues tab</a>

## Chat and Collaboration
You can always leverage <a href="https://redhathealthcare.zulipchat.com" target="_blank">Red Hat Healthcare's ZuilpChat area</a>
and find all the specific areas for iDAAS-Connect-HL7. We look forward to any feedback!!

If you would like to contribute feel free to, contributions are always welcome!!!!

Happy using and coding....
