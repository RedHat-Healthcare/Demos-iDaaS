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

As you look at this visual notice we DO NOT take a target data position to build, we believe that is a customers to shape for their specific business needs. We are working on creating a demonstration to  
show a sample endpoint for strickly closing the loop on data processing. <br/>  
Lets correlate the specific components we have designed and developed to their functional usage: <br/>

* iDaaS Connect - This is integration to data systems. We have defined this specific branding to help us be try and be specific through naming.<br/>
-iDaaS Connect HL7: This specifically ONLY deals with the ![http://www.hl7.org/implement/standards/product_section.cfm?section=13](HL7 Protocol).<br/>
-iDaaS Connect BlueButton: This is specifically designed to support the ![https://bluebutton.cms.gov/](Blue Button/CARIN) requirements and demonstrates direct integration with
CMS Blue Button platform. <br/>
-iDaaS Connect FHIR: This is intended to deal with ALL aspects of ![https://www.hl7.org/fhir/](CMS Interoperability Final Rule) efforts specific to FHIR. It is important to note that this supports both FHIR Resources and Bundles as well.<br/>
-iDaaS Connect ThirdParty: This is intended to deal with ANY non healthcare specific connectors like (File, SFTP, Queues, RDBMS, etc.). The ONLY limitation is the ![upstream connector support](https://camel.apache.org/components/latest/)<br/>
-iDaaS DREAM: This is a sample demonstration that shows intelligent routing with HL7 messages currently. The intent is to showcase how legacy integration
can be used with a modern integration capability. This allows teams to build topic based routing without adding new components or anything.<br/>
* iDaaS Route (Data Distribution) - This is all about event streaming and processing in accordance with what is needed for the reference architecture. <br/>
* iDaaS DREAM (Business Process/Parsing/Mapping) - These are reference designs intended to showcase needed industry capabilities for processing dat in a variety
of ways.
* iDaaS KIC - This is a key part of the reference implementation as it deals with auditing and transactional data tracking for ANY effort ongoing within the platform. Within healthcare auditing of transactions
is a critical phase for HIPAA compliance and it also is critical as organizations have internal teams that typically audit systems activity.

## [Connect-BlueButton](Connect-BlueButton)
This project fetches Medicare data of an authenticated beneficiary through the Blue Button API and sends it to a Kafka topic.

## [Connect-EDI](Connect-EDI)
Electronic Data Interchange (EDI) is the automated transfer of data between a care provider and a payer.  EDI enables payers and care providers to send and receive information faster, often at a lower cost.

## [Connect-FHIR](Connect-FHIR)
Demo of iDAAS Connect FHIR capabilities, this solution is intended to enable resources to get up and running and build
a quick understanding of the iDAAS Connect FHIR overall capabilities.

## [Connect-HL7](Connect-HL7)
iDAAS-Connect-HL7 specifically deals with enabling 
iDAAS to process the healthcare industry standard HL7 based transactions ONLY.

## [Connect-ThirdParty](Connect-ThirdParty)
Enables iDAAS to third party connectivity.  This includes: RDBMS, Kafka, Mainframe, Files, SFTP, and many others.

## [DREAM](DREAM)
Showcase the ability to route data real time and create new topics on demand as needed to help facilitate information processing and addressing needs for business in real time.

## [Route-DataDistribution](Route-DataDistribution)
Route Data Distribution specifically addresses data connectivity and data distribution of information.
