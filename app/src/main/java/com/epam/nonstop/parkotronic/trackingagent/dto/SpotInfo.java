package com.epam.nonstop.parkotronic.trackingagent.dto;

/**
 * Created by Alyx on 24.11.2015.
 */
public class SpotInfo {


    private String xCoordinate;
    private String yCoordinate;
    private String deviceName;
    private String deviceAdress;
    private short deviceRSSI;
    private String deviceId;

    public void setxCoordinate(String xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public String getxCoordinate() {
        return xCoordinate;
    }

    public void setyCoordinate(String yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public String getyCoordinate() {
        return yCoordinate;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceAdress(String deviceAdress) {
        this.deviceAdress = deviceAdress;
    }

    public String getDeviceAdress() {
        return deviceAdress;
    }

    public void setDeviceRSSI(short deviceRSSI) {
        this.deviceRSSI = deviceRSSI;
    }

    public short getDeviceRSSI() {
        return deviceRSSI;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }
}
