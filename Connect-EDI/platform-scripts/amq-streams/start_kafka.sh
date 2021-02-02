KAFKA_VERSION=2.6.0
mkdir tmp
cd tmp
wget "https://www.apache.org/dyn/mirrors/mirrors.cgi?action=download&filename=kafka/$KAFKA_VERSION/kafka_2.13-$KAFKA_VERSION.tgz" -O "kafka_2.13-$KAFKA_VERSION.tgz" -nc
tar -xzf "kafka_2.13-$KAFKA_VERSION.tgz"
cd "kafka_2.13-$KAFKA_VERSION"

bin/zookeeper-server-start.sh config/zookeeper.properties > /dev/null 2>&1 &
bin/kafka-server-start.sh config/server.properties > /dev/null 2>&1 &
