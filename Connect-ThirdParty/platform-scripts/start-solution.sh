cd amq-streams
echo "Starting Kafka in background"
./start_kafka.sh
cd ../..
cd target
java -jar demo-idaas-connect-thirdparty.jar
