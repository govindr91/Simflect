package com.codetagging.simflect.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by govind on 26/05/16.
 */
public class TelephonyProvider implements Serializable {

    @SerializedName("manufacturer")
    private String manufacturer;
    @SerializedName("className")
    private String className;
    @SerializedName("classInstanceMethod")
    private String classInstanceMethod;
    @SerializedName("classInstanceParamDataType")
    private Class classInstanceParamDataType;
    @SerializedName("classInstanceParamValue")
    private Object classInstanceParamValue;
    /*private List<MethodProperties> methods = new ArrayList<>();*/

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassInstanceMethod() {
        return classInstanceMethod;
    }

    public void setClassInstanceMethod(String classInstanceMethod) {
        this.classInstanceMethod = classInstanceMethod;
    }

    public Class getClassInstanceParamDataType() {
        return classInstanceParamDataType;
    }

    public void setClassInstanceParamDataType(Class classInstanceParamDataType) {
        this.classInstanceParamDataType = classInstanceParamDataType;
    }

    public Object getClassInstanceParamValue() {
        return classInstanceParamValue;
    }

    public void setClassInstanceParamValue(Object classInstanceParamValue) {
        this.classInstanceParamValue = classInstanceParamValue;
    }

    /*public List<MethodProperties> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodProperties> methods) {
        this.methods = methods;
    }*/
}
