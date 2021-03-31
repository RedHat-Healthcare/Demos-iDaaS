# Demos-iDaaS
Complete iDaaS Repository for all Demos of iDaaS. This repository is intended to showcase all the iDaaS platform
components, the caveat is these specific demonstrations are slimmed down from the full repository design pattern to
enable resources to quickly showcase capabilities offered.

# Pre-Requisites
For all iDaaS design patterns it should be assumed that you will either install as part of this effort, or have the following:

1. An existing Kafka (or some flavor of it) up and running. Red Hat currently implements AMQ-Streams based on Apache Kafka; however, we
have implemented iDaaS with numerous Kafka implementations. Please see the following files we have included to try and help: <br/>
[Kafka](Kafka.md)<br/>
[KafkaWindows](KafkaWindows.md)<br/>
2. Some understanding of building, deploying Java artifacts and the commands associated
3. An internet connection with active internet connectivity, this is to ensure that if any Maven commands are
run and any libraries need to be pulled down they can.

We also leverage [Kafka Tools](https://kafkatool.com/) to help us show Kafka details and transactions; however, you can leverage
code or various other Kafka technologies ot view the topics.

# General Architecture
The following section is meant to define and also help visualize the general architecture within an healthcare  
(payer/provider/life sciences) implementation.
![https://github.com/RedHat-Healthcare/iDAAS/blob/master/content/images/iDAAS-Platform/iDAAS%20Platform%20-%20Visuals%20-%20iDaaS%20Data%20Flow%20-%20Detailed.png](https://github.com/RedHat-Healthcare/iDAAS/blob/master/content/images/iDAAS-Platform/iDAAS%20Platform%20-%20Visuals%20-%20iDaaS%20Data%20Flow%20-%20Detailed.png)

As you look at this visual notice we DO NOT take a target data position to build toward, we believe that is a customers to shape for their specific business needs. We are working on creating a demonstration to show a sample endpoint for strickly closing the loop on data processing. <br/>  

# iDaaS Demos
This section attempts to explain the specific iDaas Demos. It is important to know that each demo application typically is much smaller that the full code asset typically, this is because we want the demos to be leveraged to showcase a viable subset of capabilities. Within each demo there is all the needed supporting materials including visuals and detailed scenarios.

## iDaaS KIC (Knowledge, Insight and Conformance)
This brand is specifically implemented to enable transactional insight into any data processing the components have done. This brand was developed to address compliance and conformance needs for implementations. 

### [iDaaS-KIC-Integration](iDaaS-KIC-Integration)
Shows the iDaaS KIC (Knowledge, Insight and Conformance) brand is to provide resources to see what the accelerators have processed. The platform currently supports JSON file based output and a RDBMS based implementation, the default provided is Postgres; however, it can be reconfigured to leverage ANY JDBC type 4 compliant database. You will just need to adjust the application.properties.

## iDaaS Connect
This section covers iDaaS Connect branded capabilities. This is integration to data systems. We have defined this specific branding to help us be try and be specific through naming.<br/>

### [Connect-Aggregator](Connect-Aggregator)
Connect Aggregator specifically addresses how to take data from multiple sources identify key attributes to ensure
you only send one transaaction out.

### [Connect-BlueButton](Connect-BlueButton)
This project fetches Medicare data of an authenticated beneficiary through the Blue Button API and sends it to a Kafka topic. iDaaS Connect BlueButton: This is specifically designed to support the [https://bluebutton.cms.gov/] (Blue Button/CARIN) requirements and demonstrates direct integration with
CMS Blue Button platform. <br/>

### [Connect-EDI](Connect-EDI)
Electronic Data Interchange (EDI) is the automated transfer of data between a care provider and a payer.  EDI enables payers and care providers to send and receive information faster, often at a lower cost.

### [Connect-FHIR](Connect-FHIR)
Demo of iDAAS Connect FHIR capabilities, this solution is intended to enable resources to get up and running and build
a quick understanding of the iDAAS Connect FHIR overall capabilities. This is intended to deal with ALL aspects of [https://www.hl7.org/fhir/] (CMS Interoperability Final Rule) efforts specific to FHIR. It is important to note that this supports both FHIR Resources and Bundles as well.<br/>

### [Connect-FHIR-DataTagging](Connect-FHIR-DataTagging)
Demo of iDAAS Connect FHIR capabilities and invokes real time data parsing and transformation with FHIR data.

### [Connect-HL7](Connect-HL7)
iDAAS-Connect-HL7 specifically deals with enabling iDAAS to process the healthcare industry standard HL7 based transactions ONLY.  [http://www.hl7.org/implement/standards/product_section.cfm?section=13] (HL7 Protocol).<br/>

### [Connect-ThirdParty](Connect-ThirdParty)
Enables iDAAS to third party connectivity.  This includes: RDBMS, Kafka, Mainframe, Files, SFTP, and many others.The ONLY limitation is the [https://camel.apache.org/components/latest/] (Camel Supported Adapters)<br/>

## iDaaS Route

### [Route-DataDistribution](Route-DataDistribution)
Route Data Distribution specifically addresses data connectivity and data distribution of information.

## iDaaS DREAM
The iDaaS DREAM brand deals with branded assets that are built through business proces/decision management, process automation or complex event processing.

### [DREAM](DREAM)
Showcase the ability to route data real time and create new topics on demand as needed to help facilitate information processing and addressing needs for business in real time.
