echo "The current working directory: $PWD"
# Changing to local directory on my machine
cd /Users/alscott/RedHatTech/
echo "starting Kafka"
./start_Kafka.sh &
echo "The current working directory: $PWD"
cd $PWD
cd ../..
cd target
echo "The current working directory: $PWD"
java -jar idaas-connect-datadistribution.jar