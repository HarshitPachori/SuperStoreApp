package com.app.superdistributor.sr.dealerorders;

public class DealerOrder {
    String Name, ProductID, ProductQty, DealerName, Status;

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

    public String getDealerName() {
        return DealerName;
    }

    public void setDealerName(String dealerName) {
        DealerName = dealerName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
