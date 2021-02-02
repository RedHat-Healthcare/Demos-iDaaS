kafkaDir=$HOME'/RedHatTech/kafka_2.12-2.5.0.redhat-00003'
echo "Directory: "$kafkaDir
cd $kafkaDir

cd $kafkaDir
bin/kafka-server-stop.sh config/server.properties &
