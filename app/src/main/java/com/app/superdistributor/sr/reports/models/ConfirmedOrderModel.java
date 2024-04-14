package com.app.superdistributor.sr.reports.models;

public class ConfirmedOrderModel {
    String Name,DealerName,ProductID,ProductQty,Status,Timestamp;

    public ConfirmedOrderModel() {
    }

    public ConfirmedOrderModel(String name, String dealerName, String productID, String productQty, String status, String timestamp) {
        Name = name;
        DealerName = dealerName;
        ProductID = productID;
        ProductQty = productQty;
        Status = status;
        Timestamp = timestamp;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDealerName() {
        return DealerName;
    }

    public void setDealerName(String dealerName) {
        DealerName = dealerName;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getProductQty() {
        return ProductQty;
    }

    public void setProductQty(String productQty) {
        ProductQty = productQty;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ConfirmedOrderModel{" +
                "Name='" + Name + '\'' +
                ", DealerName='" + DealerName + '\'' +
                ", ProductID='" + ProductID + '\'' +
                ", ProductQty='" + ProductQty + '\'' +
                ", Status='" + Status + '\'' +
                ", Timestamp='" + Timestamp + '\'' +
                '}';
    }
}
