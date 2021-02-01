echo "The current working directory: $PWD"
cd AMQ-Streams
echo "starting Kafka"
./start_Kafka.sh &
echo "The current working directory: $PWD"
cd $PWD
cd ../..
cd target
echo "The current working directory: $PWD"
java -jar idaas-connect-datadistribution.jar