# Kafka Background
This file is not intended to be anything other than a specific file related to how the iDaaS
platform leverages Kafka (AMQ-Streams) and some of the types of things you can do to maintain
your development/demo or test Kafka subsystem.

# Kafka Platform Operations
Common Operations for Platform Operations. We have kept this scripts available from within  
the repo within the platform-scripts/AMQ-Streams directory.

## Starting Kafka
1. Set the Directory
kafkaDir=$HOME'/RedHatTech/kafka_2.12-2.6.0.redhat-00004'
echo "Directory: "$kafkaDir
2. Execute The commands to start the server
cd $kafkaDir
bin/zookeeper-server-start.sh config/zookeeper.properties &
bin/kafka-server-start.sh config/server.properties &

## Stopping Kafka
1. Set the Directory
kafkaDir=$HOME'/RedHatTech/kafka_2.12-2.6.0.redhat-00004'
echo "Directory: "$kafkaDir
2. Execute The commands to start the server
cd $kafkaDir
bin/kafka-server-stop.sh config/server.properties &

# Kafka Operations
We have removed all the scripts we used to provide but wanted to keep this content available for developers and resources.

## Create Topics
This is solely for education and awareness, post Kafka 2.5 all the topics we leverage and utilize are
created if not in place as the application runs and processes data.

1. Set the Directory
kafkaDir=$HOME'/RedHatTech/kafka_2.12-2.5.0.redhat-00003'
echo "Directory: "$kafkaDir
2. Execute The commands to connect to the server and the specific topic(s)
cd $kafkaDir
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic opsmgmt_platformtransactions &

## List Topics
1. Set the Directory
kafkaDir=$HOME'/RedHatTech/kafka_2.12-2.5.0.redhat-00003'
echo "Directory: "$kafkaDir
2. Execute The commands to connect to the server and the specific topic(s)
cd $kafkaDir
bin/kafka-topics.sh --list --bootstrap-server localhost:9092 &

## Deleting Topics
1. Set the Directory
kafkaDir=$HOME'/RedHatTech/kafka_2.12-2.5.0.redhat-00003'
echo "Directory: "$kafkaDir
2. Execute The commands to connect to the server and the specific topic(s)
cd $kafkaDir
bin/kafka-topics.sh --delete --topic opsmgmt_platformtransactions &

