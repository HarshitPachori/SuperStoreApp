package com.app.superdistributor.ui.dealer_home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DealerHomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;


    public DealerHomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}