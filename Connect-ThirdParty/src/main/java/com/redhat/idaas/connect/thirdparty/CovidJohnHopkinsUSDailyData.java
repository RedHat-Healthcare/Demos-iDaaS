package com.redhat.idaas.connect.thirdparty;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@CsvRecord(separator = "\\|")
public class CovidJohnHopkinsUSDailyData {

    // Province_State,Country_Region,Last_Update,Lat,Long_,Confirmed,Deaths,Recovered,Active,FIPS,Incident_Rate,
    // People_Tested,People_Hospitalized,Mortality_Rate,UID,ISO3,Testing_Rate,Hospitalization_Rate

    @DataField(pos = 1)
    private String organizationId;

    @DataField(pos = 2)
    private String patientAccount;

    @DataField(pos = 3)
    private String patientName;

    @DataField(pos = 4)
    private String zipCode;

    @DataField(pos = 5)
    private String roomBed;

    @DataField(pos = 6)
    private int age;

    @DataField(pos = 7)
    private String gender;

    //@DataField(pos = 8, pattern="yyyy-MM-dd")
    //@JsonFormat(pattern = "yyyy-MM-dd")
    //private Date admissionDate;
    @DataField(pos = 8)
    private String admissionDate;

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getPatientAccount() {
        return patientAccount;
    }

    public void setPatientAccount(String patientAccount) {
        this.patientAccount = patientAccount;
    }

}
