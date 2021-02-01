# Change Directory to solution on local machine
echo $PWD
echo "Demo iDAAS Connect FHIR"
cd $PWD
cd ../

/usr/local/bin/mvn clean install
echo "Maven Build Completed"
/usr/local/bin/mvn package
echo "Maven Release Completed"
cd target
cp idaas-connect-*.jar demo-idaas-connect-fhir.jar
echo "Copied Release Specific Version to General version"
