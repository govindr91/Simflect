package com.codetagging.simflect.builder;

import android.content.Context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.codetagging.simflect.R;
import com.codetagging.simflect.SimflectApplication;
import com.codetagging.simflect.models.Enum;
import com.codetagging.simflect.models.MethodProperties;
import com.codetagging.simflect.models.TelephonyProvider;
import com.codetagging.simflect.util.Constants;

/**
 * Created by govind on 26/05/16.
 */
public class TelephonyProviderBuilder {

    private static List<TelephonyProvider> telephonyProviderList = null;

    public static List<TelephonyProvider> getTelephonyProviderList() {
        if(telephonyProviderList == null) {
            telephonyProviderList = buildList();
            return telephonyProviderList;
        }
        return telephonyProviderList;
    }

    public static List<MethodProperties> getGenericMethods() {

        List<MethodProperties> methodPropertiesList = new ArrayList<>();
        MethodProperties methodProperties = createMethodProperty("getDeviceId", Enum.methodType.deviceID, null, null);
        methodPropertiesList.add(methodProperties);

        methodProperties = createMethodProperty("getDeviceId", Enum.methodType.deviceID, int.class, 1);
        methodPropertiesList.add(methodProperties);

        methodProperties = createMethodProperty("getDeviceIdGemini", Enum.methodType.deviceID, int.class, 1);
        methodPropertiesList.add(methodProperties);

        methodProperties = createMethodProperty("getNetworkOperatorName", Enum.methodType.operatorName, null, null);
        methodPropertiesList.add(methodProperties);

        methodProperties = createMethodProperty("getNetworkOperatorName", Enum.methodType.operatorName, int.class, 1);
        methodPropertiesList.add(methodProperties);

        methodProperties = createMethodProperty("getNetworkOperatorName", Enum.methodType.operatorName, long.class, 2);
        methodPropertiesList.add(methodProperties);

        methodProperties = createMethodProperty("getNetworkOperatorNameGemini", Enum.methodType.operatorName, int.class, 1);
        methodPropertiesList.add(methodProperties);

        methodProperties = createMethodProperty("getSimOperatorName", Enum.methodType.operatorName, null, null);
        methodPropertiesList.add(methodProperties);

        methodProperties = createMethodProperty("getSimOperatorName", Enum.methodType.operatorName, int.class, 1);
        methodPropertiesList.add(methodProperties);

        methodProperties = createMethodProperty("getSimOperatorName", Enum.methodType.operatorName, long.class, 2);
        methodPropertiesList.add(methodProperties);

        methodProperties = createMethodProperty("getSimOperatorNameGemini", Enum.methodType.operatorName, int.class, 1);
        methodPropertiesList.add(methodProperties);

        methodProperties = createMethodProperty("getNetworkOperator", Enum.methodType.operatorDetail, null, null);
        methodPropertiesList.add(methodProperties);

        methodProperties = createMethodProperty("getNetworkOperator", Enum.methodType.operatorDetail, int.class, 1);
        methodPropertiesList.add(methodProperties);

        methodProperties = createMethodProperty("getNetworkOperator", Enum.methodType.operatorDetail, long.class, 2);
        methodPropertiesList.add(methodProperties);

        methodProperties = createMethodProperty("getNetworkOperatorGemini", Enum.methodType.operatorDetail, int.class, 1);
        methodPropertiesList.add(methodProperties);

        methodProperties = createMethodProperty("getSimOperator", Enum.methodType.operatorDetail, null, null);
        methodPropertiesList.add(methodProperties);

        methodProperties = createMethodProperty("getSimOperator", Enum.methodType.operatorDetail, int.class, 1);
        methodPropertiesList.add(methodProperties);

        methodProperties = createMethodProperty("getSimOperator", Enum.methodType.operatorDetail, long.class, 2);
        methodPropertiesList.add(methodProperties);

        methodProperties = createMethodProperty("getSimOperatorGemini", Enum.methodType.operatorDetail, int.class, 1);
        methodPropertiesList.add(methodProperties);

        methodProperties = createMethodProperty("getSimState", Enum.methodType.simState, null, null);
        methodPropertiesList.add(methodProperties);

        methodProperties = createMethodProperty("getSimState", Enum.methodType.simState, int.class, 1);
        methodPropertiesList.add(methodProperties);

        methodProperties = createMethodProperty("getSimState", Enum.methodType.simState, long.class, 2);
        methodPropertiesList.add(methodProperties);

        methodProperties = createMethodProperty("getSimStateGemini", Enum.methodType.simState, int.class, 1);
        methodPropertiesList.add(methodProperties);

        methodProperties = createMethodProperty("getSubscriberInfo", Enum.methodType.subscriptionInfo, null, null);
        methodPropertiesList.add(methodProperties);

        methodProperties = createMethodProperty("getSubscriberInfo", Enum.methodType.subscriptionInfo, int.class, 1);
        methodPropertiesList.add(methodProperties);
        return methodPropertiesList;
    }

    private static List<TelephonyProvider> buildList() {
        List<TelephonyProvider> telephonyProviders = new ArrayList<>();
        List<MethodProperties> genericMethods = getGenericMethods();

        TelephonyProvider telephonyProvider = createTelephonyProvider("default",
                Constants.DEFAULT_CLASS, "getDefault", null, null, genericMethods);
        telephonyProviders.add(telephonyProvider);

        telephonyProvider = createTelephonyProvider("default",
                Constants.DEFAULT_CLASS, "getDefault", int.class, 1, genericMethods);
        telephonyProviders.add(telephonyProvider);

        telephonyProvider = createTelephonyProvider("default",
                Constants.MEDIATEK_CLASS, "TelephonyManagerEx", Context.class, null, genericMethods);
        telephonyProviders.add(telephonyProvider);

        telephonyProvider = createTelephonyProvider("asus",
                Constants.DEFAULT_CLASS, "get2ndTm", null, null, genericMethods);
        telephonyProviders.add(telephonyProvider);

        telephonyProvider = createTelephonyProvider("asus",
                Constants.DEFAULT_CLASS, "getTmBySlot", int.class, 1, genericMethods);
        telephonyProviders.add(telephonyProvider);

        telephonyProvider = createTelephonyProvider("samsung",
                Constants.SAMSUNG_CLASS, "getDefault", int.class, 1, genericMethods);
        telephonyProviders.add(telephonyProvider);

        telephonyProvider = createTelephonyProvider("samsung",
                Constants.DEFAULT_CLASS, "getSecondary", null, null, genericMethods);
        telephonyProviders.add(telephonyProvider);

        return telephonyProviders;
    }

    private static MethodProperties createMethodProperty(String methodName,
                                                  Enum.methodType methodType,
                                                  Class dataType,
                                                  Object value) {
        MethodProperties methodProperties = new MethodProperties();
        methodProperties.setMethodName(methodName);
        methodProperties.setMethodType(methodType);
        methodProperties.setParamDataType(dataType);
        methodProperties.setParamValue(value);
        return methodProperties;
    }

    private static TelephonyProvider createTelephonyProvider(String manufacturer,
                                                             String className,
                                                             String classInstanceMethod,
                                                             Class classInstanceParamDataType,
                                                             Object classInstanceParamValue,
                                                             List<MethodProperties> methods) {
        TelephonyProvider telephonyProvider = new TelephonyProvider();
        telephonyProvider.setManufacturer(manufacturer);
        telephonyProvider.setClassName(className);
        telephonyProvider.setClassInstanceMethod(classInstanceMethod);
        telephonyProvider.setClassInstanceParamDataType(classInstanceParamDataType);
        telephonyProvider.setClassInstanceParamValue(classInstanceParamValue);
        /*telephonyProvider.setMethods(methods);*/
        return telephonyProvider;
    }

}
