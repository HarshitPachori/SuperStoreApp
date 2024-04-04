package com.app.superdistributor;

public class TechnicianComplaints {
    private String CustomerName;
    private String DateOfPurchase;
    private String ModelNumber;
    private String PhoneNumber;
    private String SerialNumber;
    private String Status;

    public TechnicianComplaints(String customerName, String dateOfPurchase, String modelNumber, String phoneNumber, String serialNumber, String status) {
        CustomerName = customerName;
        DateOfPurchase = dateOfPurchase;
        ModelNumber = modelNumber;
        PhoneNumber = phoneNumber;
        SerialNumber = serialNumber;
        Status = status;
    }
    public TechnicianComplaints(){}

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getDateOfPurchase() {
        return DateOfPurchase;
    }

    public void setDateOfPurchase(String dateOfPurchase) {
        DateOfPurchase = dateOfPurchase;
    }

    public String getModelNumber() {
        return ModelNumber;
    }

    public void setModelNumber(String modelNumber) {
        ModelNumber = modelNumber;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
    }


}
