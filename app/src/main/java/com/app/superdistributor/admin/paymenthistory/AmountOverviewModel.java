package com.app.superdistributor.admin.paymenthistory;

public class AmountOverviewModel {
    String Name, CurrentBalance, UserName;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCurrentBalance() {
        if(CurrentBalance == null) return "0";
        return CurrentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        CurrentBalance = currentBalance;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    @Override
    public String toString() {
        return "AmountOverviewModel{" +
                "Name='" + Name + '\'' +
                ", CurrentBalance='" + CurrentBalance + '\'' +
                ", UserName='" + UserName + '\'' +
                '}';
    }
}
