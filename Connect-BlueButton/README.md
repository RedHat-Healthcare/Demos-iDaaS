# iDAAS-Connect-BlueButton
This project fetches Medicare data of an authenticated beneficiary through the [Blue Button API](https://bluebutton.cms.gov/) and sends it to a Kafka topic. The fuse application serves as a webserver. User opens the served URL using a web browser and log into the Medicare database. The application will automatically fetch its part A, B, C, and D data and sends it to a Kafka topic. Then other processors can subscribe to the topic to process the data.

## Prerequisites
1. Sign up for the blue button [developer sandbox](https://bluebutton.cms.gov/). 
2. Create a new application with the following. Then write down the resulting Client ID and Client Secret
* OAuth - Client Type: confidential
* OAuth - Grant Type: authorization-code
* Callback URLS: http://localhost:8890/callback (or another url more appropriate)

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
