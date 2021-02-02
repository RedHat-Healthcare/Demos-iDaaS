source kafka_version.sh
cd "tmp/kafka_2.13-$KAFKA_VERSION"

bin/kafka-server-stop.sh config/server.properties
bin/zookeeper-server-stop.sh config/zookeeper.properties

rm -rf /tmp/kafka-logs /tmp/zookeeper
