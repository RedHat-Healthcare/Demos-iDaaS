# Demos-iDaaS
Complete iDaaS Repository for all Demos of iDaaS. This repository is intended to showcase all the iDaaS platform
components, the caveat is these specific demonstrations are slimmed down from the full repository design pattern to
enable resources to quickly showcase capabilities offered.

# Pre-Requisites
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
 
We also leverage [Kafka Tools](https://kafkatool.com/) to help us show Kafka details and transactions..

# Start The Engine!!!
This section covers the running any of the design patterns/accelerators. There are several options to start the Engine Up!!!

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
# General Architecture
The following section is meant to define and also help visualize the general architecture within an healthcare  
(payer/provider/life sciences) implementation.
![https://github.com/RedHat-Healthcare/iDAAS/blob/master/content/images/iDAAS-Platform/iDAAS%20Platform%20-%20Visuals%20-%20iDaaS%20Data%20Flow%20-%20Detailed.png](https://github.com/RedHat-Healthcare/iDAAS/blob/master/content/images/iDAAS-Platform/iDAAS%20Platform%20-%20Visuals%20-%20iDaaS%20Data%20Flow%20-%20Detailed.png)

As you look at this visual notice we DO NOT take a target data position to build toward, we believe that is a customers to shape for their specific business needs. We are working on creating a demonstration to show a sample endpoint for strickly closing the loop on data processing. <br/>  

# iDaaS Demos
This section attempts to explain the specific iDaas Demos. It is important to know that each demo application typically is much smaller that the full code asset typically, this is because we want the demos to be leveraged to showcase a viable subset of capabilities. Within each demo there is all the needed supporting materials including visuals and detailed scenarios.

## iDaaS KIC (Knowledge, Insight and Conformance)
This brand is specifically implemented to enable transactional insight into any data processing the components have done. This brand was developed to address compliance and conformance needs for implementations. 

### [iDaaS-KIC-Integration](https://github.com/RedHat-Healthcare/iDaaS-Demos/tree/master/Route-DataDistribution)
Shows the iDaaS KIC (Knowledge, Insight and Conformance) brand is to provide resources to see what the accelerators have processed. The platform currently supports JSON file based output and a RDBMS based implementation, the default provided is Postgres; however, it can be reconfigured to leverage ANY JDBC type 4 compliant database. You will just need to adjust the application.properties.

## iDaaS Connect
This section covers iDaaS Connect branded capabilities. This is integration to data systems. We have defined this specific branding to help us be try and be specific through naming.<br/>

### [Connect-Aggregator](https://github.com/RedHat-Healthcare/iDaaS-Demos/tree/master/Connect-Aggregator)
Connect Aggregator specifically addresses how to take data from multiple sources identify key attributes to ensure
you only send one transaaction out.

### [Connect-BlueButton](https://github.com/RedHat-Healthcare/iDaaS-Demos/tree/master/Connect-BlueButton)
This project fetches Medicare data of an authenticated beneficiary through the Blue Button API and sends it to a Kafka topic. iDaaS Connect BlueButton: This is specifically designed to support the [https://bluebutton.cms.gov/] (Blue Button/CARIN) requirements and demonstrates direct integration with
CMS Blue Button platform. <br/>

### [Connect-EDI](https://github.com/RedHat-Healthcare/iDaaS-Demos/tree/master/Connect-EDI)
Electronic Data Interchange (EDI) is the automated transfer of data between a care provider and a payer.  EDI enables payers and care providers to send and receive information faster, often at a lower cost.

### [Connect-FHIR](https://github.com/RedHat-Healthcare/iDaaS-Demos/tree/master/Connect-FHIR)
Demo of iDAAS Connect FHIR capabilities, this solution is intended to enable resources to get up and running and build
a quick understanding of the iDAAS Connect FHIR overall capabilities. This is intended to deal with ALL aspects of [https://www.hl7.org/fhir/] (CMS Interoperability Final Rule) efforts specific to FHIR. It is important to note that this supports both FHIR Resources and Bundles as well.<br/>

### [Connect-FHIR-DataTagging](https://github.com/RedHat-Healthcare/iDaaS-Demos/tree/master/Connect-FHIR-DataTagging)
Demo of iDAAS Connect FHIR capabilities and invokes real time data parsing and transformation with FHIR data.

### [Connect-HL7](https://github.com/RedHat-Healthcare/iDaaS-Demos/tree/master/Connect-HL7)
iDAAS-Connect-HL7 specifically deals with enabling iDAAS to process the healthcare industry standard HL7 based transactions ONLY.  [http://www.hl7.org/implement/standards/product_section.cfm?section=13] (HL7 Protocol).<br/>

### [Connect-ThirdParty](https://github.com/RedHat-Healthcare/iDaaS-Demos/tree/master/Connect-ThirdParty)
Enables iDAAS to third party connectivity.  This includes: RDBMS, Kafka, Mainframe, Files, SFTP, and many others.The ONLY limitation is the [https://camel.apache.org/components/latest/] (Camel Supported Adapters)<br/>

## iDaaS Route

### [Route-DataDistribution](https://github.com/RedHat-Healthcare/iDaaS-Demos/tree/master/Route-DataDistribution)
Route Data Distribution specifically addresses data connectivity and data distribution of information.

## iDaaS DREAM
The iDaaS DREAM brand deals with branded assets that are built through business proces/decision management, process automation or complex event processing.

### [DREAM](https://github.com/RedHat-Healthcare/iDaaS-Demos/tree/master/DREAM)
Showcase the ability to route data real time and create new topics on demand as needed to help facilitate information processing and addressing needs for business in real time.
