package com.app.superdistributor.sr.reports.models;

public class RegisteredComplaintModel {
    String CustomerName,DateOfPurchase,ModelNumber,PhoneNumber,ReportUrl,SerialNumber,Status;

    public RegisteredComplaintModel() {
    }

    public RegisteredComplaintModel(String customerName, String dateOfPurchase, String modelNumber, String phoneNumber, String reportUrl, String serialNumber, String status) {
        CustomerName = customerName;
        DateOfPurchase = dateOfPurchase;
        ModelNumber = modelNumber;
        PhoneNumber = phoneNumber;
        ReportUrl = reportUrl;
        SerialNumber = serialNumber;
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

    public String getReportUrl() {
        return ReportUrl;
    }

    public void setReportUrl(String reportUrl) {
        ReportUrl = reportUrl;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    @Override
    public String toString() {
        return "RegisteredComplaintModel{" +
                "CustomerName='" + CustomerName + '\'' +
                ", DateOfPurchase='" + DateOfPurchase + '\'' +
                ", ModelNumber='" + ModelNumber + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", ReportUrl='" + ReportUrl + '\'' +
                ", SerialNumber='" + SerialNumber + '\'' +
                ", Status='" + Status + '\'' +
                '}';
    }
}
