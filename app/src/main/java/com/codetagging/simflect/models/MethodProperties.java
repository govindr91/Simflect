package com.codetagging.simflect.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by govind on 26/05/16.
 */
public class MethodProperties implements Serializable {

    @SerializedName("methodName")
    private String methodName;
    @SerializedName("methodType")
    private Enum.methodType methodType;
    @SerializedName("paramDataType")
    private Class paramDataType;
    @SerializedName("paramValues")
    private Object paramValue;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class getParamDataType() {
        return paramDataType;
    }

    public void setParamDataType(Class paramDataType) {
        this.paramDataType = paramDataType;
    }

    public Enum.methodType getMethodType() {
        return methodType;
    }

    public void setMethodType(Enum.methodType methodType) {
        this.methodType = methodType;
    }

    public Object getParamValue() {
        return paramValue;
    }

    public void setParamValue(Object paramValue) {
        this.paramValue = paramValue;
    }
}
