package com.app.superdistributor.sr.reports.models;

public class ReplaceByDealerModel {
    String CustomerName, DateOfPurchase, NewProductSerialNumber, PhoneNumber, ReportUrl, SerialNumber, Status;

    public ReplaceByDealerModel() {
    }

    public ReplaceByDealerModel(String customerName, String dateOfPurchase, String newProductSerialNumber, String phoneNumber, String reportUrl, String serialNumber, String status) {
        CustomerName = customerName;
        DateOfPurchase = dateOfPurchase;
        NewProductSerialNumber = newProductSerialNumber;
        PhoneNumber = phoneNumber;
        ReportUrl = reportUrl;
        SerialNumber = serialNumber;
        Status = status;
    }

    @Override
    public String toString() {
        return "ReplaceByDealerModel{" +
                "CustomerName='" + CustomerName + '\'' +
                ", DateOfPurchase='" + DateOfPurchase + '\'' +
                ", NewProductSerialNumber='" + NewProductSerialNumber + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", ReportUrl='" + ReportUrl + '\'' +
                ", SerialNumber='" + SerialNumber + '\'' +
                ", Status='" + Status + '\'' +
                '}';
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

    public String getNewProductSerialNumber() {
        return NewProductSerialNumber;
    }

    public void setNewProductSerialNumber(String newProductSerialNumber) {
        NewProductSerialNumber = newProductSerialNumber;
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
}
