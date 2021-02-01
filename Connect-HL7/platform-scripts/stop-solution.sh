echo "Stoping Kafka running in background"
cd amq-streams
./stop_kafka.sh
echo "Stoping iDaas - Connect HL7"
cd ../..
cd target
kill $(cat ./bin/shutdown.pid)

