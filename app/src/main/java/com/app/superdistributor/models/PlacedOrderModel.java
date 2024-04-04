package com.app.superdistributor.models;

public class PlacedOrderModel {
    String DealerName, Name, ProductID, ProductQty;

    public PlacedOrderModel() {
    }

    public PlacedOrderModel(String dealerName, String name, String productID, String productQty) {
        DealerName = dealerName;
        Name = name;
        ProductID = productID;
        ProductQty = productQty;
    }

    public String getDealerName() {
        return DealerName;
    }

    public void setDealerName(String dealerName) {
        DealerName = dealerName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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

    @Override
    public String toString() {
        return "PlacedOrderModel{" +
                "DealerName='" + DealerName + '\'' +
                ", Name='" + Name + '\'' +
                ", ProductID='" + ProductID + '\'' +
                ", ProductQty='" + ProductQty + '\'' +
                '}';
    }
}
