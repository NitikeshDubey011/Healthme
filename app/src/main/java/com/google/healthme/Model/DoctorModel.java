package com.google.healthme.Model;

public class DoctorModel {

    String doctorName,doctorQualification, doctorProfileImage;

    public DoctorModel() {
    }

    public DoctorModel(String doctorName, String doctorQualification, String doctorProfileImage) {
        this.doctorName = doctorName;
        this.doctorQualification = doctorQualification;
        this.doctorProfileImage = doctorProfileImage;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorQualification() {
        return doctorQualification;
    }

    public void setDoctorQualification(String doctorQualification) {
        this.doctorQualification = doctorQualification;
    }

    public String getDoctorProfileImage() {
        return doctorProfileImage;
    }

    public void setDoctorProfileImage(String doctorProfileImage) {
        this.doctorProfileImage = doctorProfileImage;
    }
}
