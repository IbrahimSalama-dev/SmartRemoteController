package com.example.smartremotecontroller;

import java.util.HashMap;

public class Device {
    private HashMap<String, RemoteButton> buttonsList = new HashMap<String, RemoteButton>();
    private String deviceName;

    public Device(){
    }

    public HashMap<String, RemoteButton> getButtonsList() {
        return buttonsList;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void addButton(RemoteButton b){
        this.buttonsList.put(b.getFunctionName(), b);
    }




}
