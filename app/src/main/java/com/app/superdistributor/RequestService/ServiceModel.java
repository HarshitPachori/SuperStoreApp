package com.app.superdistributor.RequestService;

public class ServiceModel {
    String CustomerName, DateOfPurchase, ModelNumber, NewProductSerialNumber, PhoneNumber, ReportUrl, SerialNumber;

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

    @Override
    public String toString() {
        return "ServiceModel{" +
                "CustomerName='" + CustomerName + '\'' +
                ", DateOfPurchase='" + DateOfPurchase + '\'' +
                ", ModelNumber='" + ModelNumber + '\'' +
                ", NewProductSerialNumber='" + NewProductSerialNumber + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", ReportUrl='" + ReportUrl + '\'' +
                ", SerialNumber='" + SerialNumber + '\'' +
                '}';
    }
}
