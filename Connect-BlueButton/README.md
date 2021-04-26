# iDAAS-Connect-BlueButton
This project fetches Medicare data of an authenticated beneficiary through the [Blue Button API](https://bluebutton.cms.gov/) and sends it to a Kafka topic. The fuse application serves as a webserver. User opens the served URL using a web browser and log into the Medicare database. The application will automatically fetch its part A, B, C, and D data and sends it to a Kafka topic. Then other processors can subscribe to the topic to process the data.

## General Prerequisites
1. Sign up for the blue button [developer sandbox](https://bluebutton.cms.gov/). 
2. Create a new application with the following. Then write down the resulting Client ID and Client Secret
* OAuth - Client Type: confidential
* OAuth - Grant Type: authorization-code
* Callback URLS: http://localhost:8890/callback (or another url more appropriate)

## Solution Pre-Requisities
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

# Demonstration
The following are some key steps when running this solution to a demonstration.

## Configuration
Configure src/main/resources/application.properties with the prerequisite data, for example,
```
bluebutton.callback.path=callback
bluebutton.callback.host=localhost
bluebutton.callback.port=8890
```
http://localhost:8890/callback is the callback URL you registered with bluebutton.cms.gov. http://localhost:8890/bluebutton will be the service URL for iDAAS-Connect-BlueButton. 

## Getting Started
* Download the [CSV file](https://bluebutton.cms.gov/synthetic_users_by_claim_count_full.csv) which contains 100 sample data with id, user name, and password.
* Build the project by running `platform-scripts/build-solution.sh`
* Start the project by running `platform-scripts/start-solution.sh`. It will start the Kafka cluster on port 9092 and the Fuse application which listens on port 8890.
* In a web browser type http://localhost:8890/bluebutton
* It will automatically redirect you to Blue Buttons's authentication page. Fill in the user name and password
* Patient, Coverage, and ExplanationOfBenifit data will be sent to Kafka topic `bluebutton`.
