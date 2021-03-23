# iDAAS-Data-DataHub
iDAAS DataHub platform is intended for persisting of data needed within the iDAAS. The current main usage is 
for auditing and logging activities. 

This solution contains three supporting directories. The intent of these artifacts to enable
resources to work locally: <br/>
1. platform-addons: needed software to run locally. This currently contains amq-streams-1.5 (which is the upstream of Kafka 2.5)<br/>
2. platform-scripts: support running kafka, creating/listing and deleting topics needed for this solution
and also building and packaging the solution as well. All the scripts are named to describe their capabilities <br/>
3. platform-datatier: DDL that support this implementation 

## Supported RDBMS
Here are links to the Community Edition RDBMS We are using:

<a href="https://www.mariadb.com/" target="_blank">MySQL Community Edition</a>: v8 or better<br>

## Scenario: Integration 
This repository follows a very common implementation. The implementation
is processing all the data from the topic named opmgmt_transactions.

### Integration Data Flow Steps
 
1. The Topic opmgmt_transactions is subscribed to for transactions.<br/>
2. The data will be processed from the queue and the header attributes will all be parsed.<br/>
3. The header attributes should then be persisted to the appropriate database fields in appauditing_auditlog and
   appauditing_auditlog_msgs depending upon data attributes.<br/>
    
## Builds
This section will cover both local and automated builds.

### Local Builds
Within the code base you can find the local build commands in the platform-scripts directory
1.  Run the build-solution.sh script
It will run the maven commands to build and then package up the solution. The package will use the usual settings
in the pom.xml file. It outputs target/idaas-datahub.jar

### Running
Once built you can run the solution by executing `./platform-scripts/start-solution.sh`. 
The script will startup Kafka and iDAAS DataHub Service.

Alternatively, if you have a running instance of Kafka, you can start a solution with:
`./platform-scripts/start-solution-with-external-kafka.sh --idaas.kafkaBrokers=host1:port1,host2:port2`.
The script will startup iDAAS DataHub Service.

Optionally you can configure the service to store audit data in DB. You can run an instance of Postgres db with:

`docker run --rm --name datahub-db -e POSTGRES_PASSWORD=Postgres123 -p 5432:5432 postgres:alpine`

You can enable storing audit in DB by running the service with `--idaas.storeInDb=true` parameter. You can customize 
default DB connection details by providing the following properties:
```properties
idass.dbDriverClassName=org.postgresql.Driver
idaas.dbUrl=jdbc:postgresql://localhost:5432/audit_db
idaas.dbUsername=postgres
idass.dbPassword=Postgres123
idaas.dbTableName=audit
idaas.createDbTable=true
```

It is possible to overwrite configuration by:
1. Providing parameters via command line e.g.
`./start-solution.sh --idaas.auditDir=some/other/audit/dir`
2. Creating an application.properties next to the idaas-datahub.jar in the target directory
3. Creating a properties file in a custom location `./start-solution.sh --spring.config.location=file:./config/application.properties`

Supported properties include:
```properties
server.port=8080

idaas.kafkaBrokers=localhost:9092 #a comma separated list of kafka brokers e.g. host1:port1,host2:port2
idaas.kafkaTopicName=opsmgmt_platformtransactions
idaas.auditDir=audit

idaas.storeInDb=false
idass.dbDriverClassName=org.postgresql.Driver
idaas.dbUrl=jdbc:postgresql://localhost:5432/audit_db
idaas.dbUsername=postgres
idass.dbPassword=Postgres123
ioaas.dbTableName=audit
idaas.createDbTable=true
```



### Usage

The service listens for Kafka messages on the `opsmgmt_platformtransactions` queue by default and outputs them to 
'audit' dir as json files.

The output includes both message and headers.

There's also an endpoint available for putting messages on the queue. The REST service runs on port 8080 by default, but
it can be customized by providing `--server.port=8082` parameter.
 
You can POST a message like this:
```shell script
curl --location --request POST 'http://localhost:8080/message' \
--header 'Content-Type: application/json' \
--data-raw '{
    "auditEntireMessage": "test",
    "processingtype": "data",
	"industrystd": "HL7",
	"messagetrigger": "ADT"
}'
```


### Automated Builds
Automated Builds are going to be done in Azure Pipelines

## Ongoing Enhancements
We maintain all enhancements within the Git Hub portal under the 
<a href="https://github.com/RedHat-Healthcare/iDAAS-DataHub/projects" target="_blank">projects tab</a>

## Defects/Bugs
All defects or bugs should be submitted through the Git Hub Portal under the 
<a href="https://github.com/RedHat-Healthcare/iDAAS-DataHub/issues" target="_blank">issues tab</a>

## Chat and Collaboration
You can always leverage <a href="https://redhathealthcare.zulipchat.com" target="_blank">Red Hat Healthcare's ZuilpChat area</a>
and find all the specific areas for iDAAS-DataHub. We look forward to any feedback!!

If you would like to contribute feel free to, contributions are always welcome!!!! 

Happy using and coding....
