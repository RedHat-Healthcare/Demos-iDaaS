kafkaDir='/Users/alscott/RedHatTech/kafka_2.12-2.5.0.redhat-00003'
cd $ kafkaDir

## Operational Topics for Platform
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic opsmgmt_platformtransactions &
## CSV
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic mandatedreporting &

