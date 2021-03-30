package com.example.smartremotecontroller;

import java.util.HashMap;

public class User {
    private String userName;
    private double longitude;
    private double latitude;
    private HashMap<String, Device> devicesList = new HashMap<String, Device>();

    public User() { }



    public void setUserNameUser(String userName) {
        this.userName = userName;
    }

    public void addDevice(Device d){
        this.devicesList.put(d.getDeviceName(),d);
    }

    public String getUserName() {
        return userName;
    }

    public HashMap<String, Device> getDevicesList() {
        return devicesList;
    }

    public void setDevicesList(HashMap<String, Device> devicesList) {
        this.devicesList = devicesList;
    }

    public void setDevice(String deviceName){
        Device d = new Device();
        d.setDeviceName(deviceName);
        addDevice(d);
    }

    /*public void addButtonInDevice(String deviceName, String buttonName, int functionCode){
        this.devicesList.get(deviceName).addButton(new RemoteButton(buttonName,functionCode));
    }*/
}
