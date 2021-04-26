# Demo-iDAAS-Connect-ThirdParty
Demo of iDAAS Connect Third Party Capabilities

iDAAS Connect is intended ONLY to enable iDAAS connectivity. iDAAS-Connect-ThirdParty specifically ONLY deals with enabling 
iDAAS to all sorts of third party connectivity. For example: RDBMS, Kafka, Mainframe, Files, SFTP, etc.
plus dozens of others are supported.

This is a demonstration of some of the capabilities that iDAAS-Connect-ThirdParty can enable and support. 
Currently this demo has a Kafka Topic Pull and a CSV parsing to MySQL database process. 

The intent of these artifacts to enable
resources to work locally: <br/>
+ platform-scripts: support running kafka, creating/listing and deleting topics needed for this solution
and also building and packaging the solution as well. All the scripts are named to describe their capabilities <br/>
+ platform-testdata: sample transactions to leverage for using the platform. <br/>
+ platform-ddl: The DDL for the database that is used.

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

### RDBMS (Relational Database Management Systems)
We wanted to provide a very simple way to have users be able to visualize and report on anything the iDaaS framework(s) do. So we have included a
very basic extensible relational database tier. Here are links to the Community Edition RDBMS We are using:<br/>

<a href="https://www.mariadb.com/" target="_blank">MySQL Community Edition</a>: v8 or greater<br>

You will need to create a database within either technology and have all the credentials for the database.

# Pre-Setup (Before Running Demo)
For this demonstration we have done the following steps.

1.  Created a MySQL Database named idaas and implemented the DDL found within the platform-ddl into this database
2.  We have created a user idaas with a password @idaas123
3.  We have made sure this user has ALL permissions
4.  Run the scripts in the platform-ddl directory

We would expect that since these are all parameters within the application.properties file you can change these to your needs and liking.

## Scenario: CSV File Processing
This repository follows a very common general implementation of processing a CSV file from a filesystem. The intent is to pick up
a bar '|' delimited file and process it into a structure, persist the data into a topic and then process it into a database table.

### Integration Data Flow Steps
 
1. Every 1 minute the defined directory is looked at for any .CSV file, if found the file is processed into a matching structure.
2. The data structure is then persisted into a kafka topic. 
3. The kafka topic is connected to and the transactions are then persisted into a specific database table.
    
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

The following is the existing application.properties file. It guides this accelerator as to what internal port to use for internal activities and http efforts, where kafka is located, all the needed directories to process files
from and also the database configuration:
```
# Server - Internal
server.host=9983
# Kafka
kafkaBrokers=localhost:9092
# Reporting Directory and File Name
mandatory.reporting.directory=src/data/MandatoryReporting
mandatory.reporting.file=ReportingExample.csv
# Covid Directory and File Ext
covid.reporting.directory=src/data/CovidData
covid.reporting.extension=*.csv
# Reseach Data Directory and File Ext
research.data.directory=src/data/ResearchData
# JDBC Database
spring.datasource.url=jdbc:mysql://localhost/idaas
#spring.datasource.url=jdbc:postgresql://localhost:5432/idaas
spring.datasource.username=idaas
spring.datasource.password=@idaas123
```

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
