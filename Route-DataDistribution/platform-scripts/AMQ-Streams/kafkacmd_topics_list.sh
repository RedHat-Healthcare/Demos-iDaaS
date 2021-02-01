kafkaDir=$HOME'/RedHatTech/kafka_2.12-2.5.0.redhat-00003'
echo "Directory: "$kafkaDir
cd $kafkaDir

bin/kafka-topics.sh --list --bootstrap-server localhost:9092 &

