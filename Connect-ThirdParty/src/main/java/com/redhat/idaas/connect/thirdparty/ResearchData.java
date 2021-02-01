package com.redhat.idaas.connect.thirdparty;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@CsvRecord(separator = "\\|")
public class ResearchData {

    // https://loinc.org/sars-coronavirus-2/
    // https://loinc.org/sars-cov-2-and-covid-19/
    // https://www.cdc.gov/csels/dls/locs/2020/livd_codes_for_sars-cov-2_antigen_tests.html
    // https://www.hhs.gov/sites/default/files/non-lab-based-covid19-test-reporting.pdf

    // Zipcode|Gender|DOB|Age|FacilityID|OrganizationName|LabCode|LabValue
    @DataField(pos = 1)
    private String zipCode;

    @DataField(pos = 2)
    private String gender;

    @DataField(pos = 3)
    private String dateOfBirth;

    @DataField(pos = 4)
    private String patientAge;

    @DataField(pos = 5)
    private String facilityId;

    @DataField(pos = 6)
    private String organizationName;

    @DataField(pos = 7)
    private String labCode;

    @DataField(pos = 8)
    private String labValue;

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getLabCode() {
        return labCode;
    }

    public void setLabCode(String labCode) {
        this.labCode = labCode;
    }

    public String getLabValue() {
        return labValue;
    }

    public void setLabValue(String labValue) {
        this.labValue = labValue;
    }
}
