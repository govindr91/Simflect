package codetagging.simflect.models;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import codetagging.simflect.R;
import codetagging.simflect.SimflectApplication;
import codetagging.simflect.util.Constants;

/**
 * Created by govind on 23/05/16.
 */
public class TelephonyProviders implements Serializable {

    private static TelephonyProviders telephonyProviders = null;

    private HashMap<String, List<String>> deviceIdMethods = new HashMap<>();
    private HashMap<String, List<String>> simStateMethods = new HashMap<>();
    private HashMap<String, List<String>> networkOperatorMethods = new HashMap<>();
    private HashMap<String, List<String>> networkOperatorNameMethods = new HashMap<>();
    private HashMap<String, List<String>> simOperatorMethods = new HashMap<>();
    private HashMap<String, List<String>> simOperatorNameMethods = new HashMap<>();
    private ArrayList<String> telephonyClasses = new ArrayList<>();


    public static TelephonyProviders newInstance() {
        if(telephonyProviders == null)
            return new TelephonyProviders();
        return telephonyProviders;
    }

    TelephonyProviders() {

        telephonyClasses.add(Constants.DEFAULT_CLASS);
        telephonyClasses.add(Constants.SAMSUNG_CLASS);

        /*
        * for phone type: asus, lenovo, huwaei, one plus (may support other manufacturers too)
        * */
        List<String> deviceMethodList = new ArrayList<>();
        deviceMethodList.add("getDeviceId");
        deviceMethodList.add("getDeviceIdGemini");
        deviceMethodList.add("getDeviceIdDs");
        deviceIdMethods.put("android.telephony.TelephonyManager", deviceMethodList);

        List<String> simStateList = new ArrayList<>();
        simStateList.add("getSimState");
        simStateList.add("getSimStateGemini");
        simStateList.add("getSimStateDs");
        simStateMethods.put("android.telephony.TelephonyManager", simStateList);

        List<String> simOpsList = new ArrayList<>();
        simOpsList.add("getSimOperator");
        simOpsList.add("getSimOperatorGemini");
        simOperatorMethods.put("android.telephony.TelephonyManager", simOpsList);

        List<String> simOpsNameList = new ArrayList<>();
        simOpsNameList.add("getSimOperatorName");
        simOperatorNameMethods.put("android.telephony.TelephonyManager", simOpsNameList);

        List<String> networkOpsList = new ArrayList<>();
        networkOpsList.add("getNetworkOperator");
        networkOpsList.add("getNetworkOperatorGemini");
        networkOpsList.add("getNetworkOperatorExt");
        networkOperatorMethods.put("android.telephony.TelephonyManager", networkOpsList);

        List<String> networkOpsNameList = new ArrayList<>();
        networkOpsNameList.add("getNetworkOperatorName");
        networkOperatorNameMethods.put("android.telephony.TelephonyManager", networkOpsNameList);

        /*
        * for samsung devices
        * */
        List<String> samsungMethodList = new ArrayList<>();
        samsungMethodList.add("getDeviceId");
        samsungMethodList.add("getDeviceIdDs"); //takes int as param
        deviceIdMethods.put("android.telephony.MultiSimTelephonyManager", samsungMethodList);

        List<String> samsungSimStateList = new ArrayList<>();
        samsungSimStateList.add("getSimState");
        samsungSimStateList.add("getSimStateDs"); //takes int as param
        simStateMethods.put("android.telephony.MultiSimTelephonyManager", samsungSimStateList);

        List<String> samsungSimOpsList = new ArrayList<>();
        samsungSimOpsList.add("getSimOperator");
        simOperatorMethods.put("android.telephony.MultiSimTelephonyManager", samsungSimOpsList);

        List<String> samsungSimOpsNameList = new ArrayList<>();
        samsungSimOpsNameList.add("getSimOperatorName");
        simOperatorNameMethods.put("android.telephony.MultiSimTelephonyManager", samsungSimOpsNameList);

        List<String> samsungNetworkOpsList = new ArrayList<>();
        samsungNetworkOpsList.add("getNetworkOperator");
        networkOperatorMethods.put("android.telephony.MultiSimTelephonyManager", samsungNetworkOpsList);

        List<String> samsungNetworkOpsNameList = new ArrayList<>();
        samsungNetworkOpsNameList.add("getNetworkOperatorName");
        networkOperatorNameMethods.put("android.telephony.MultiSimTelephonyManager", samsungNetworkOpsNameList);

    }

    public HashMap<String, List<String>> getDeviceIdMethods() {
        return deviceIdMethods;
    }

    public HashMap<String, List<String>> getSimStateMethods() {
        return simStateMethods;
    }

    public HashMap<String, List<String>> getNetworkOperatorMethods() {
        return networkOperatorMethods;
    }

    public HashMap<String, List<String>> getNetworkOperatorNameMethods() {
        return networkOperatorNameMethods;
    }

    public HashMap<String, List<String>> getSimOperatorMethods() {
        return simOperatorMethods;
    }

    public HashMap<String, List<String>> getSimOperatorNameMethods() {
        return simOperatorNameMethods;
    }

    public ArrayList<String> getTelephonyClasses() {
        return telephonyClasses;
    }
}
