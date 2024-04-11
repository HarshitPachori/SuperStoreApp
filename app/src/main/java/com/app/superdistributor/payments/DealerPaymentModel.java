package com.app.superdistributor.payments;

public class DealerPaymentModel {
    String paymentMethod,username,amount,toUser,status;

    public DealerPaymentModel(String paymentMethod, String username, String amount, String toUser, String status) {
        this.paymentMethod = paymentMethod;
        this.username = username;
        this.amount = amount;
        this.toUser = toUser;
        this.status = status;
    }

    public DealerPaymentModel() {
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "DealerPaymentModel{" +
                "paymentMethod='" + paymentMethod + '\'' +
                ", username='" + username + '\'' +
                ", amount='" + amount + '\'' +
                ", toUser='" + toUser + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
