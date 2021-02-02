cd amq-streams
echo "Starting Kafka in background"
./start_kafka.sh
cd ../..
cd target
java -jar idaas-connect-hl7.jar $@
