# iDAAS-KIC (Knowledge, INsight and Conformance)
iDAAS DataHub platform is intended for persisting of data needed within the iDAAS. The current main usage is 
for auditing and logging activities. 

## Add-Ons
This solution contains three supporting directories. The intent of these artifacts to enable
resources to work locally: <br/>
+ platform-datatier: DDL that support this implementation 

## Pre-Requisites
For all iDaaS design patterns it should be assumed that you will either install as part of this effort, or have the following:
1. An existing Kafka (or some flavor of it) up and running. Red Hat currently implements AMQ-Streams based on Apache Kafka; however, we
have implemented iDaaS with numerous Kafka implementations. Please see the following files we have included to try and help: <br/>
[Kafka](https://github.com/RedHat-Healthcare/iDaaS-Demos/blob/master/Kafka.md)<br/>
[KafkaWindows](https://github.com/RedHat-Healthcare/iDaaS-Demos/blob/master/KafkaWindows.md)<br/>
No matter the platform chosen it is important to know that the Kafka out of the box implementation might require some changes depending
upon your implementation needs. Here are a few we have made to ensure: <br/>
In <kafka>/config/consumer.properties file we will be enhancing the property of auto.offset.reset to earliest. This is intended to enable any new 
system entering the group to read ALL the messages from the start. <br/>
auto.offset.reset=earliest <br/>
2. Some understanding of building, deploying Java artifacts and the commands associated. If using Maven commands then Maven would need to be intalled and runing for the environment you are using. More details about Maven can be found [here](https://maven.apache.org/install.html)<br/>
3. An internet connection with active internet connectivity, this is to ensure that if any Maven commands are
run and any libraries need to be pulled down they can.<br/>

We also leverage [Kafka Tools](https://kafkatool.com/) to help us show Kafka details and transactions; however, you can leverage
code or various other Kafka technologies ot view the topics.

# RDBMS (Relational Database Management Systems)
We wanted to provide a very simple way to have users be able to visualize and report on anything the iDaaS framework(s) do. So we have included a
very basic extensible relational database tier. Here are links to the Community Edition RDBMS We are using:<br/>

<a href="https://www.mariadb.com/" target="_blank">MySQL Community Edition</a>: v8 or greater<br>
<a href="https://www.postgresql.org" target="_blank">PostgreSQL</a>: v9.6 or greater<br>

You will need to create a database within either technology and have all the credentials for the database.

## Database Implementation
To implement the relational database. You do not need to implement it as you can merely setup transactions to be processed and routed out as  
JSON events and processed to whatever technology makes the implementation happy.
1. Within the Supported RDBMS we need to create a user and password and then keep the information for later in the configuration process.
2. Run the DDL for the database from platform-datatier (latest release for your RDBMS)
3. Configure the application.properties
4. Start up and start processing data

## Platform Implementation

## Scenario: Integration 
The implementation processes all the data from the topic named opmgmt_transactions.

## Integration Data Flow Steps
1. The integration application subscribes to all the data from the topic opmgmt_transactions topic.<br/>
2. The data will be processed from the topic and the header attributes will all be parsed.<br/>
3. Depending on the settings in the application.properties the header attributes can go to files, a relational
database table named insight within the kic database or both. These actions are done through parsing the header attributes.<br/>
    
# Builds
This section will cover both local and automated builds.

## Local Builds
Within the code base you can find the local build commands in the platform-scripts directory
1.  Run the build-solution.sh script
It will run the maven commands to build and then package up the solution. The package will use the usual settings
in the pom.xml file. It outputs target/idaas-datahub.jar

## Running
Once built you can run the solution by executing `./platform-scripts/start-solution.sh`. 
The script will startup Kafka and iDAAS DataHub Service.

You can enable storing audit in DB by running the service with `--idaas.storeInDb=true` parameter. You can customize
default DB connection details by providing the following properties: <br/>
mySQL Property Example<br/>:
```properties
# MySQL
idaas.dbDriverClassName=com.mysql.cj.jdbc.Driver
idaas.dbUrl=jdbc:mysql://localhost:3306/kic?useLegacyDatetimeCode=false&serverTimezone=GMT
idaas.dbPassword=Developer123
idaas.dbUsername=root
idaas.dbTableName=insight
```
Postgres Property Example<br/>
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
idaas.createDbTable=false
```
Alternatively, if you have a running instance of Kafka, you can start a solution with:
`./platform-scripts/start-solution-with-external-kafka.sh --idaas.kafkaBrokers=host1:port1,host2:port2`.
The script will startup iDAAS DataHub Service.

Optionally you can configure the service to store audit data in DB. You can run an instance of Postgres db with:

For Postgres:
`docker run --rm --name datahub-db -e POSTGRES_PASSWORD=Postgres123 -p 5432:5432 postgres:alpine`

### Usage

The service listens for Kafka messages on the `opsmgmt_platformtransactions` queue by default and outputs them to 
'audit' dir as json files.

The output includes both message and headers.

There's also an endpoint available for putting messages on the queue. The REST service runs on port 8080 by default, but
it can be customized by providing `--server.port=8082` parameter.

Testing:<br/>
In order for any testing to work you must:

1. Have a Kafka server up and ready to process data to/from.
2. Start the iDaaS-KIC platform (multiple ways)
3. you can POST a message like this:
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
4. You can use the iDaaS-Simulator-KIC which simply runs and transacts to the iDaaS-KIC tier when files move.


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
