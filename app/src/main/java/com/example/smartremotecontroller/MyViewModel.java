package com.example.smartremotecontroller;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class MyViewModel extends AndroidViewModel {
    private DatabaseInterface databaseInterface;
    private MutableLiveData<User> liveData;
    private MutableLiveData<String> liveCode;

    public MyViewModel(@NonNull Application application){
        super(application);
        databaseInterface = new DatabaseInterface();
        liveData = databaseInterface.getLiveUser();
        liveCode = databaseInterface.getLiveCode();
    }
    public MutableLiveData<User> getData(){
        return liveData;
    }
    public void addDevice(String deviceName){
        databaseInterface.addDevice(deviceName);
    }

    public void addButton(String buttonName, String deviceName, int code){
        databaseInterface.addButton(buttonName, deviceName, code);
    }

    public void removeButtons(String[] buttonsToRemove, String deviceName){
        databaseInterface.removeButtons(buttonsToRemove,deviceName);
    }

    public void removeDevices(String[] devicesToRemove){
        databaseInterface.removeDevices(devicesToRemove);
    }

    public void changeDeviceName(String newName, String oldName){
        databaseInterface.changeDeviceName(newName, oldName);
    }

    public void changeButtonName(String newName, String oldName, String deviceName){
        databaseInterface.changeButtonName(newName, oldName, deviceName);
    }

    public MutableLiveData<String> getLiveCode() {
        return liveCode;
    }

    public void performButtonFunction(String buttonName, String deviceName){
        databaseInterface.performButtonFunction(buttonName, deviceName);
    }
}
