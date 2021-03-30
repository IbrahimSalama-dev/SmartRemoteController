package com.example.smartremotecontroller;

import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DatabaseInterface{
    private User user;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private LocationManager locationManager;
    private MutableLiveData<User> liveUser;
    private MutableLiveData<String> liveCode;
    private int code;



    public DatabaseInterface(){
        database= FirebaseDatabase.getInstance();
        myRef = database.getReference();
        liveUser = new MutableLiveData<User>();
        liveCode = new MutableLiveData<String>();
        myRef.child("code").push();
        myRef.child("code").setValue(0);
    }

    public MutableLiveData<User> getLiveUser() {
        databaseFetchUser();
        return liveUser;
    }

    public MutableLiveData<String> getLiveCode() {
        databaseFetchCode();
        return liveCode;
    }

    public String databaseFetchCode() {
        Log.d("mycode", "databaseFetchCode: ");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.getKey()!=null) {
                        if(ds.getKey().equals("code")){
                            code = ds.getValue(Integer.class).intValue();
                            liveCode.setValue(Integer.toString(code));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return Integer.toString(code);
    }

    public User databaseFetchUser(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.getKey() != null) {
                        if(ds.getKey().equals("user")){
                            user = ds.getValue(User.class);
                            liveUser.setValue(user);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(user!=null)
            Log.d("dbudate", user.getUserName());
        return user;
    }

    public void changeDeviceName(String newName, String oldName){
        Device d = user.getDevicesList().get(oldName);
        d.setDeviceName(newName);
        user.getDevicesList().remove(oldName);
        user.addDevice(d);
        myRef.child("user").push();
        myRef.child("user").setValue(user);

    }

    public void changeButtonName(String newName, String oldName, String deviceName){
        RemoteButton b = user.getDevicesList().get(deviceName).getButtonsList().get(oldName);
        b.setFunctionName(newName);
        user.getDevicesList().get(deviceName).getButtonsList().remove(oldName);
        user.getDevicesList().get(deviceName).addButton(b);
        myRef.child("user").push();
        myRef.child("user").setValue(user);

    }

    public void databaseWrite(){
        User u = new User();
        u.setUserNameUser("hi");
        Device d = new Device();
        d.setDeviceName("d1");
        u.addDevice(d);
        myRef.child("user").push();
        myRef.child("user").setValue(u);

    }

    public void addDevice(String deviceName){
        Device d = new Device();
        d.setDeviceName(deviceName);
        user.addDevice(d);
        myRef.child("user").push();
        myRef.child("user").setValue(user);
    }

    public void addButton(String buttonName, String deviceName, int buttonCode){
        RemoteButton b = new RemoteButton();
        b.setFunctionName(buttonName);
        b.setFunctionCode(buttonCode);
        user.getDevicesList().get(deviceName).addButton(b);
        myRef.child("user").push();
        myRef.child("user").setValue(user);
        myRef.child("code").push();
        myRef.child("code").setValue(0);
    }

    public void removeButtons(String[] buttonsToRemove, String deviceName){
        for(int i = 0; i < buttonsToRemove.length; i++){
            user.getDevicesList().get(deviceName).getButtonsList().remove(buttonsToRemove[i]);
        }
        myRef.child("user").push();
        myRef.child("user").setValue(user);
    }

    public void removeDevices(String[] devicesToRemove){
        for(int i = 0; i < devicesToRemove.length; i++){
            user.getDevicesList().remove(devicesToRemove[i]);
        }
        myRef.child("user").push();
        myRef.child("user").setValue(user);
    }

    public void performButtonFunction(String buttonName, String deviceName){
        myRef.child("code").push();
        myRef.child("code").setValue(user.getDevicesList().get(deviceName).getButtonsList().get(buttonName).getFunctionCode());
    }

}
