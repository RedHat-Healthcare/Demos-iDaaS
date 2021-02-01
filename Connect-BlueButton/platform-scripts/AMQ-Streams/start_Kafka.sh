kafkaDir='/Users/alscott/RedHatTech/kafka_2.12-2.5.0.redhat-00003'
cd $ kafkaDir

bin/zookeeper-server-start.sh config/zookeeper.properties &
bin/kafka-server-start.sh config/server.properties &
