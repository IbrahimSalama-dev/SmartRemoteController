package com.example.smartremotecontroller;

public class RemoteButton {
    private String functionName;
    private int functionCode;

    public RemoteButton() { }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public int getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(int functionCode) {
        this.functionCode = functionCode;
    }


}
