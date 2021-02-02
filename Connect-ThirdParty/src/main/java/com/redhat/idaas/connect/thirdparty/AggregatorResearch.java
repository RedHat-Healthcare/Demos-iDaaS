package com.redhat.idaas.connect.thirdparty;
// Imports
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@CsvRecord(separator = "\\|")
public class AggregatorResearch {
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
    private String reportedDateTime;

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

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getRoomBed() {
        return roomBed;
    }

    public void setRoomBed(String roomBed) {
        this.roomBed = roomBed;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    /* public Date getAdmissionDate() {
         return admissionDate;
     }

     public void setAdmissionDate(Date admissionDate) {
         this.admissionDate = admissionDate;
     }
     */
    public String getReportedDateTime() {
        return reportedDateTime;
    }

    public void setReportedDateTime(String reportedDate) {
        this.reportedDateTime = reportedDateTime;
    }
}
