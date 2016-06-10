package codetagging.simflect.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by govind on 23/05/16.
 */
public class PhoneDetails implements Serializable {

    @SerializedName("manufacturer")
    private String manufacturer;
    @SerializedName("model")
    private String model;
    @SerializedName("versionCode")
    private int versionCode;

    @SerializedName("isDualSIM")
    private boolean isDualSIM;
    @SerializedName("isFirstSimReady")
    private boolean isFirstSimReady;
    @SerializedName("isSecondSimReady")
    private boolean isSecondSimReady;

    @SerializedName("firstSimID")
    private String firstSimID;
    @SerializedName("secondSimID")
    private String secondSimID;

    @SerializedName("firstSIMOperatorName")
    private String firstSimOperatorName;
    @SerializedName("secondSIMOperatorName")
    private String secondSimOperatorName;

    @SerializedName("firstSimMCC")
    private int firstSimMCC;
    @SerializedName("secondSimMCC")
    private int secondSimMCC;

    @SerializedName("firstSimMNC")
    private int firstSimMNC;
    @SerializedName("secondSimMNC")
    private int secondSimMNC;

    public PhoneDetails() {
        manufacturer = null;
        model = null;
        versionCode = -1;
        isDualSIM = false;
        isFirstSimReady = false;
        isSecondSimReady = false;
        firstSimID = null;
        secondSimID = null;
        firstSimOperatorName = null;
        secondSimOperatorName = null;
        firstSimMCC = -1;
        firstSimMNC = -1;
        secondSimMCC = -1;
        secondSimMNC = -1;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public boolean isDualSIM() {
        return isDualSIM;
    }

    public void setIsDualSIM(boolean isDualSIM) {
        this.isDualSIM = isDualSIM;
    }

    public boolean isFirstSimReady() {
        return isFirstSimReady;
    }

    public void setIsFirstSimReady(boolean isFirstSimReady) {
        this.isFirstSimReady = isFirstSimReady;
    }

    public boolean isSecondSimReady() {
        return isSecondSimReady;
    }

    public void setIsSecondSimReady(boolean isSecondSimReady) {
        this.isSecondSimReady = isSecondSimReady;
    }

    public String getFirstSimID() {
        return firstSimID;
    }

    public void setFirstSimID(String firstSimID) {
        this.firstSimID = firstSimID;
    }

    public String getSecondSimID() {
        return secondSimID;
    }

    public void setSecondSimID(String secondSimID) {
        this.secondSimID = secondSimID;
    }

    public String getFirstSimOperatorName() {
        return firstSimOperatorName;
    }

    public void setFirstSimOperatorName(String firstSimOperatorName) {
        this.firstSimOperatorName = firstSimOperatorName;
    }

    public String getSecondSimOperatorName() {
        return secondSimOperatorName;
    }

    public void setSecondSimOperatorName(String secondSimOperatorName) {
        this.secondSimOperatorName = secondSimOperatorName;
    }

    public int getFirstSimMCC() {
        return firstSimMCC;
    }

    public void setFirstSimMCC(int firstSimMCC) {
        this.firstSimMCC = firstSimMCC;
    }

    public int getSecondSimMCC() {
        return secondSimMCC;
    }

    public void setSecondSimMCC(int secondSimMCC) {
        this.secondSimMCC = secondSimMCC;
    }

    public int getFirstSimMNC() {
        return firstSimMNC;
    }

    public void setFirstSimMNC(int firstSimMNC) {
        this.firstSimMNC = firstSimMNC;
    }

    public int getSecondSimMNC() {
        return secondSimMNC;
    }

    public void setSecondSimMNC(int secondSimMNC) {
        this.secondSimMNC = secondSimMNC;
    }


}
