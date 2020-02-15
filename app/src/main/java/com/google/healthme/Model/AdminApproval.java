package com.google.healthme.Model;

public class AdminApproval {
    boolean approved;

    public AdminApproval(boolean approved) {
        this.approved = approved;
    }

    public AdminApproval() {
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
