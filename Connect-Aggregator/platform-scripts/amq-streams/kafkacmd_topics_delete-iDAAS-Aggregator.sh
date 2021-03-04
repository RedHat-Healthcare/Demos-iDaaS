kafkaDir='/Users/alscott/RedHatTech/kafka_2.12-2.5.0.redhat-00003'
cd $ kafkaDir

## Operational Topics for Platform
bin/kafka-topics.sh --delete --bootstrap-server localhost:9092 --topic opsmgmt_platformtransactions &
## CSV
bin/kafka-topics.sh --delete --bootstrap-server localhost:9092 --topic mandatedreporting &
