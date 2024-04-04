package com.app.superdistributor.admin.notification;

public class ProductConfirmationModel {
    String Name, PlacedBy, Price, ProductID, Qty;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPlacedBy() {
        return PlacedBy;
    }

    public void setPlacedBy(String placedBy) {
        PlacedBy = placedBy;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }
}
