package com.google.healthme.Model;

public class MemberDoctorModel {
    String full_name,dob,gender,address,city,state,pincode,country,profile_image,qualification,doctor_category,type;


    public MemberDoctorModel(String full_name, String dob, String gender, String address, String city, String state,
                             String pincode, String country,
                             String doctor_category, String type) {
        this.full_name = full_name;
        this.dob = dob;
        this.gender = gender;
        this.address = address;
        this.city = city;
        this.type=type;
        this.state = state;
        this.pincode = pincode;
        this.country = country;
//        this.profile_image = profile_image;
//        this.qualification = qualification;
        this.doctor_category = doctor_category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MemberDoctorModel() {
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getDoctor_category() {
        return doctor_category;
    }

    public void setDoctor_category(String doctor_category) {
        this.doctor_category = doctor_category;
    }
}
