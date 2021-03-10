echo "Stoping Kafka running in background"
cd amq-streams
./stop_kafka.sh
echo "Stopping Demo iDaas Connect Aggregator"
cd ../..
cd target
kill $(cat ./bin/shutdown.pid)

