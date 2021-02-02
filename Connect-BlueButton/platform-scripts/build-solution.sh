# Change Directory to solution on local machine
echo $PWD
echo "iDAAS - Connect HL7"
cd $PWD
cd ../

/usr/local/bin/mvn clean install
echo "Maven Build Completed"
/usr/local/bin/mvn package
echo "Maven Release Completed"
cd target
cp idaas-connect-*.jar idaas-connect-hl7.jar
echo "Copied Release Specific Version to General version"
