package codetagging.simflect.builder;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.List;

import codetagging.simflect.SimManager;
import codetagging.simflect.models.MethodProperties;
import codetagging.simflect.models.PhoneDetails;
import codetagging.simflect.models.TelephonyProvider;
import codetagging.simflect.util.Constants;

/**
 * Created by govind on 24/05/16.
 */
public class SimflectBuilder {

    public static void buildDetail(Context context, SimManager.setOnPhoneDetailsFetched setOnPhoneDetailsFetched) {
        BuildPhoneDetails buildPhoneDetails = new BuildPhoneDetails(context, setOnPhoneDetailsFetched);
        buildPhoneDetails.execute();
    }

    private static class BuildPhoneDetails extends AsyncTask<Void, Void, Void> {

        Context context;
        SimManager.setOnPhoneDetailsFetched setOnPhoneDetailsFetched;
        PhoneDetails phoneDetails;

        BuildPhoneDetails(Context context, SimManager.setOnPhoneDetailsFetched setOnPhoneDetailsFetched) {
            this.context = context;
            this.setOnPhoneDetailsFetched = setOnPhoneDetailsFetched;
            phoneDetails = new PhoneDetails();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String manufacturer = Build.MANUFACTURER;
            String model = Build.MODEL;
            int version = Build.VERSION.SDK_INT;
            phoneDetails.setManufacturer(manufacturer);
            phoneDetails.setModel(model);
            phoneDetails.setVersionCode(version);
            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            //    configurePostLollipop(context);
            //} else {
                configurePreLollipop(context);
            //}
            return null;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
        private void configurePostLollipop(Context context) {
            SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            List<SubscriptionInfo> subscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
            if (subscriptionInfoList != null) {
                //get sim 1 info
                SubscriptionInfo subscriptionInfo = subscriptionInfoList.get(0);
                phoneDetails.setFirstSimID(String.valueOf(subscriptionInfo.getIccId()));
                phoneDetails.setIsFirstSimReady(true);
                phoneDetails.setFirstSimOperatorName(subscriptionInfo.getDisplayName().toString());
                phoneDetails.setFirstSimMCC(subscriptionInfo.getMcc());
                phoneDetails.setFirstSimMNC(subscriptionInfo.getMnc());

                if (subscriptionInfoList.size() > 1) {
                    //get sim2 info
                    subscriptionInfo = subscriptionInfoList.get(1);
                    phoneDetails.setSecondSimID(String.valueOf(subscriptionInfo.getIccId()));
                    phoneDetails.setIsSecondSimReady(true);
                    phoneDetails.setSecondSimOperatorName(subscriptionInfo.getDisplayName().toString());
                    phoneDetails.setSecondSimMCC(subscriptionInfo.getMcc());
                    phoneDetails.setSecondSimMNC(subscriptionInfo.getMnc());
                    phoneDetails.setIsDualSIM(true);
                } else {
                    phoneDetails.setIsDualSIM(false);
                }
                return;
            } else {
                configurePreLollipop(context);
            }
        }

        private void configurePreLollipop(Context context) {

            TelephonyManager telephonyManager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
            phoneDetails.setFirstSimID(telephonyManager.getDeviceId());
            phoneDetails.setIsFirstSimReady(telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY);
            phoneDetails.setFirstSimOperatorName(telephonyManager.getNetworkOperatorName());
            try {
                String operatorDetail = telephonyManager.getNetworkOperator();
                int mcc = Integer.parseInt(operatorDetail.substring(0, 3));
                int mnc = Integer.parseInt(operatorDetail.substring(3));
                phoneDetails.setFirstSimMCC(mcc);
                phoneDetails.setFirstSimMNC(mnc);
            } catch (Exception e) {
                e.printStackTrace();
            }

            boolean isIMEIFetched = false;
            boolean isStateFetched = false;
            boolean isOperatorNameFetched = false;
            boolean isOperatorDetailFetched = false;

            for (TelephonyProvider telephonyProvider : TelephonyProviderBuilder.getTelephonyProviderList()) {

                /*String manufacturer = Build.MANUFACTURER.toLowerCase();
                if (manufacturer.equalsIgnoreCase("samsung") && !telephonyProvider.getManufacturer().equalsIgnoreCase("samsung")) {
                    continue;
                }*/

                Object classObject = null;
                if(TypeReflection.isClassAvailable(telephonyProvider.getClassName())) {
                    try {
                        classObject = TypeReflection.getClassStaticInstance(telephonyProvider.getClassName(),
                                telephonyProvider.getClassInstanceMethod(), telephonyProvider.getClassInstanceParamValue(),
                                telephonyProvider.getClassInstanceParamDataType());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    continue;
                }

                boolean isFirstSimObjectHitPossible = false;
                if(telephonyProvider.getClassName().equalsIgnoreCase(Constants.DEFAULT_CLASS) &&
                        telephonyProvider.getClassInstanceMethod().equalsIgnoreCase("getDefault") &&
                        telephonyProvider.getClassInstanceParamDataType() == null) {
                    isFirstSimObjectHitPossible = true;
                }

                for (MethodProperties methodProperties : telephonyProvider.getMethods()) {
                    if(isFirstSimObjectHitPossible && methodProperties.getParamDataType() == null) {
                        continue;
                    }
                    switch (methodProperties.getMethodType()) {
                        case deviceID: {
                            if (!isIMEIFetched) {
                                try {
                                    if (TypeReflection.isMethodAvailable(classObject, methodProperties.getMethodName(), methodProperties.getParamDataType())) {
                                        String imei = (String) TypeReflection.invokeMethod(classObject, methodProperties.getMethodName(),
                                                methodProperties.getParamValue(), methodProperties.getParamDataType());
                                        if (!TextUtils.isEmpty(imei)) {
                                            phoneDetails.setSecondSimID(imei);
                                            isIMEIFetched = true;
                                        }
                                    }
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                        case operatorName: {
                            if (!isOperatorNameFetched) {
                                try {
                                    if (TypeReflection.isMethodAvailable(classObject, methodProperties.getMethodName(), methodProperties.getParamDataType())) {
                                        String name = (String) TypeReflection.invokeMethod(classObject, methodProperties.getMethodName(),
                                                methodProperties.getParamValue(), methodProperties.getParamDataType());
                                        if (!TextUtils.isEmpty(name)) {
                                            phoneDetails.setSecondSimOperatorName(name);
                                            isOperatorNameFetched = true;
                                        }
                                    }
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                        case operatorDetail: {
                            if (!isOperatorDetailFetched) {
                                try {
                                    if (TypeReflection.isMethodAvailable(classObject, methodProperties.getMethodName(), methodProperties.getParamDataType())) {
                                        String detail = (String) TypeReflection.invokeMethod(classObject, methodProperties.getMethodName(),
                                                methodProperties.getParamValue(), methodProperties.getParamDataType());
                                        if (!TextUtils.isEmpty(detail)) {
                                            int mcc = Integer.parseInt(detail.substring(0, 3));
                                            int mnc = Integer.parseInt(detail.substring(3));
                                            phoneDetails.setSecondSimMCC(mcc);
                                            phoneDetails.setSecondSimMNC(mnc);
                                            isOperatorDetailFetched = true;
                                        }
                                    }
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                        case simState: {
                            if (!isStateFetched) {
                                try {
                                    if (TypeReflection.isMethodAvailable(classObject, methodProperties.getMethodName(), methodProperties.getParamDataType())) {
                                        boolean simState = TypeReflection.invokeMethod(classObject, methodProperties.getMethodName(),
                                                methodProperties.getParamValue(), methodProperties.getParamDataType()).equals((Object) 5);
                                        phoneDetails.setIsSecondSimReady(simState);
                                        isStateFetched = true;
                                    }
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                        case subscriptionInfo: {
                            if (!isIMEIFetched) {
                                try {
                                    if (TypeReflection.isMethodAvailable(classObject, methodProperties.getMethodName(), methodProperties.getParamDataType())) {
                                        Class interfaceClass = (Class) TypeReflection.invokeMethod(classObject, methodProperties.getMethodName(),
                                                methodProperties.getParamValue(), methodProperties.getParamDataType());
                                        String deviceId = (String) TypeReflection.invokeMethod(interfaceClass, "getDeviceId", null, null);
                                        if (!TextUtils.isEmpty(deviceId)) {
                                            phoneDetails.setSecondSimID(deviceId);
                                            isIMEIFetched = true;
                                        }
                                    }
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    }
                    if (isIMEIFetched && isOperatorDetailFetched && isOperatorNameFetched && isStateFetched) {
                        break;
                    }
                }
                if (isIMEIFetched && isOperatorDetailFetched && isOperatorNameFetched && isStateFetched) {
                    break;
                }
            }

            /*for(String telephonyClass : telephonyProviders.getTelephonyClasses()) {

                ArrayList<String> deviceIdMethods = (ArrayList<String>) telephonyProviders.getDeviceIdMethods().get(telephonyClass);
                ArrayList<String> simStateMethods = (ArrayList<String>) telephonyProviders.getSimStateMethods().get(telephonyClass);
                ArrayList<String> simOpsMethods = (ArrayList<String>) telephonyProviders.getSimOperatorMethods().get(telephonyClass);
                ArrayList<String> simOpsNameMethods = (ArrayList<String>) telephonyProviders.getSimOperatorNameMethods().get(telephonyClass);
                ArrayList<String> netOpsMethods = (ArrayList<String>) telephonyProviders.getNetworkOperatorMethods().get(telephonyClass);
                ArrayList<String> netOpsNameMethods = (ArrayList<String>) telephonyProviders.getNetworkOperatorNameMethods().get(telephonyClass);

                Object firstSimClassObject = null;
                Object secondSimClassObject = null;
                try {
                    firstSimClassObject = TypeReflection.getClassContextInstance(telephonyClass, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(firstSimClassObject == null) {
                    try {
                        firstSimClassObject = TypeReflection.getClassStaticInstance(telephonyClass, "getDefault", 0, int.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                try {
                    secondSimClassObject = TypeReflection.getClassStaticInstance(telephonyClass, "getDefault", 1, int.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(secondSimClassObject == null) {
                    try {
                        secondSimClassObject = TypeReflection.getClassStaticInstance(telephonyClass, "getSecondary", null, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                for (String deviceIDMethod : deviceIdMethods) {
                    try {
                        phoneDetails.setSecondSimID((String) TypeReflection.invokeMethod(secondSimClassObject, deviceIDMethod, 1, int.class));
                        break;
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(TextUtils.isEmpty(phoneDetails.getSecondSimID())) {
                    for (String deviceIDMethod : deviceIdMethods) {
                        try {
                            phoneDetails.setSecondSimID((String) TypeReflection.invokeMethod(secondSimClassObject, deviceIDMethod, null, null));
                            break;
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                for (String simStateMethod : simStateMethods) {
                    try {
                        phoneDetails.setIsSecondSimReady(TypeReflection.invokeMethod(secondSimClassObject, simStateMethod, 1, int.class).equals((Object) 5));
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(!phoneDetails.isSecondSimReady()) {
                    for (String simStateMethod : simStateMethods) {
                        try {
                            phoneDetails.setIsSecondSimReady(TypeReflection.invokeMethod(secondSimClassObject, simStateMethod, null, null).equals((Object) 5));
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                for (String operatorNameMethod : netOpsNameMethods) {
                    try {
                        phoneDetails.setSecondSimOperatorName((String) TypeReflection.invokeMethod(secondSimClassObject, operatorNameMethod, 1, int.class));
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(TextUtils.isEmpty(phoneDetails.getSecondSimOperatorName())) {
                    for (String operatorNameMethod : netOpsNameMethods) {
                        try {
                            phoneDetails.setSecondSimOperatorName((String) TypeReflection.invokeMethod(secondSimClassObject, operatorNameMethod, null, null));
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (TextUtils.isEmpty(phoneDetails.getSecondSimOperatorName())) {
                    for (String operatorNameMethod : netOpsNameMethods) {
                        try {
                            phoneDetails.setSecondSimOperatorName((String) TypeReflection.invokeMethod(secondSimClassObject, operatorNameMethod, 2, long.class));
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (TextUtils.isEmpty(phoneDetails.getSecondSimOperatorName())) {
                    for (String operatorNameMethod : simOpsNameMethods) {
                        try {
                            phoneDetails.setSecondSimOperatorName((String) TypeReflection.invokeMethod(secondSimClassObject, operatorNameMethod, null, null));
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (TextUtils.isEmpty(phoneDetails.getSecondSimOperatorName())) {
                    for (String operatorNameMethod : simOpsNameMethods) {
                        try {
                            phoneDetails.setSecondSimOperatorName((String) TypeReflection.invokeMethod(secondSimClassObject, operatorNameMethod, 1, int.class));
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (TextUtils.isEmpty(phoneDetails.getSecondSimOperatorName())) {
                    for (String operatorNameMethod : simOpsNameMethods) {
                        try {
                            phoneDetails.setSecondSimOperatorName((String) TypeReflection.invokeMethod(secondSimClassObject, operatorNameMethod, 2, long.class));
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                try {
                    String operatorInfoSIM1 = telephonyManager.getNetworkOperator();
                    int mccSIM1 = Integer.parseInt(operatorInfoSIM1.substring(0, 3));
                    int mncSIM1 = Integer.parseInt(operatorInfoSIM1.substring(3));
                    phoneDetails.setFirstSimMCC(mccSIM1);
                    phoneDetails.setFirstSimMNC(mncSIM1);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                for (String operatorMethod : netOpsMethods) {
                    try {
                        if(phoneDetails.getFirstSimMCC() == -1) {
                            String operatorInfoSIM1 = null;
                            operatorInfoSIM1 = (String) TypeReflection.invokeMethod(firstSimClassObject, operatorMethod, 0, int.class);
                            if (operatorInfoSIM1 != null) {
                                if (!operatorInfoSIM1.isEmpty()) {
                                    int mccSIM1 = Integer.parseInt(operatorInfoSIM1.substring(0, 3));
                                    int mncSIM1 = Integer.parseInt(operatorInfoSIM1.substring(3));
                                    phoneDetails.setFirstSimMCC(mccSIM1);
                                    phoneDetails.setFirstSimMNC(mncSIM1);
                                }
                            }
                        }

                        String operatorInfoSIM2 = null;
                        operatorInfoSIM2 = (String) TypeReflection.invokeMethod(secondSimClassObject, operatorMethod, 1, int.class);
                        if (operatorInfoSIM2 != null)
                            if (!operatorInfoSIM2.isEmpty()) {
                                int mccSIM2 = Integer.parseInt(operatorInfoSIM2.substring(0, 3));
                                int mncSIM2 = Integer.parseInt(operatorInfoSIM2.substring(3));
                                phoneDetails.setSecondSimMCC(mccSIM2);
                                phoneDetails.setSecondSimMNC(mncSIM2);
                            }
                        break;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(phoneDetails.getSecondSimMCC() == -1) {
                    for (String operatorMethod : netOpsMethods) {
                        try {
                            if(phoneDetails.getFirstSimMCC() == -1) {
                                String operatorInfoSIM1 = null;
                                operatorInfoSIM1 = (String) TypeReflection.invokeMethod(firstSimClassObject, operatorMethod, null, null);
                                if (operatorInfoSIM1 != null)
                                    if (!operatorInfoSIM1.isEmpty()) {
                                        int mccSIM1 = Integer.parseInt(operatorInfoSIM1.substring(0, 3));
                                        int mncSIM1 = Integer.parseInt(operatorInfoSIM1.substring(3));
                                        phoneDetails.setFirstSimMCC(mccSIM1);
                                        phoneDetails.setFirstSimMNC(mncSIM1);
                                    }
                            }

                            String operatorInfoSIM2 = null;
                            operatorInfoSIM2 = (String) TypeReflection.invokeMethod(secondSimClassObject, operatorMethod, null, null);
                            if (operatorInfoSIM2 != null) {
                                if (!operatorInfoSIM2.isEmpty()) {
                                    int mccSIM2 = Integer.parseInt(operatorInfoSIM2.substring(0, 3));
                                    int mncSIM2 = Integer.parseInt(operatorInfoSIM2.substring(3));
                                    phoneDetails.setSecondSimMCC(mccSIM2);
                                    phoneDetails.setSecondSimMNC(mncSIM2);
                                }
                            }
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                if(phoneDetails.getSecondSimMCC() == -1) {
                    for (String operatorMethod : netOpsMethods) {
                        try {
                            if(phoneDetails.getFirstSimMCC() == -1) {
                                String operatorInfoSIM1 = null;
                                operatorInfoSIM1 = (String) TypeReflection.invokeMethod(firstSimClassObject, operatorMethod, 1, long.class);
                                if (operatorInfoSIM1 != null)
                                    if (!operatorInfoSIM1.isEmpty()) {
                                        int mccSIM1 = Integer.parseInt(operatorInfoSIM1.substring(0, 3));
                                        int mncSIM1 = Integer.parseInt(operatorInfoSIM1.substring(3));
                                        phoneDetails.setFirstSimMCC(mccSIM1);
                                        phoneDetails.setFirstSimMNC(mncSIM1);
                                    }
                            }

                            String operatorInfoSIM2 = null;
                            operatorInfoSIM2 = (String) TypeReflection.invokeMethod(secondSimClassObject, operatorMethod, 2, long.class);
                            if (operatorInfoSIM2 != null) {
                                if (!operatorInfoSIM2.isEmpty()) {
                                    int mccSIM2 = Integer.parseInt(operatorInfoSIM2.substring(0, 3));
                                    int mncSIM2 = Integer.parseInt(operatorInfoSIM2.substring(3));
                                    phoneDetails.setSecondSimMCC(mccSIM2);
                                    phoneDetails.setSecondSimMNC(mncSIM2);
                                }
                            }
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                if(phoneDetails.getSecondSimMCC() == -1) {
                    for (String operatorMethod : simOpsMethods) {
                        try {
                            if(phoneDetails.getFirstSimMCC() == -1) {
                                String operatorInfoSIM1 = (String) TypeReflection.invokeMethod(firstSimClassObject, operatorMethod, null, null);
                                if (operatorInfoSIM1 != null) {
                                    if (!operatorInfoSIM1.isEmpty()) {
                                        int mccSIM1 = Integer.parseInt(operatorInfoSIM1.substring(0, 3));
                                        int mncSIM1 = Integer.parseInt(operatorInfoSIM1.substring(3));
                                        phoneDetails.setFirstSimMCC(mccSIM1);
                                        phoneDetails.setFirstSimMNC(mncSIM1);
                                    }
                                }
                            }

                            String operatorInfoSIM2 = (String) TypeReflection.invokeMethod(secondSimClassObject, operatorMethod, null, null);
                            if (operatorInfoSIM2 != null) {
                                if (!operatorInfoSIM2.isEmpty()) {
                                    int mccSIM2 = Integer.parseInt(operatorInfoSIM2.substring(0, 3));
                                    int mncSIM2 = Integer.parseInt(operatorInfoSIM2.substring(3));
                                    phoneDetails.setSecondSimMCC(mccSIM2);
                                    phoneDetails.setSecondSimMNC(mncSIM2);
                                }
                            }
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                if(phoneDetails.getSecondSimMCC() == -1) {
                    for (String operatorMethod : simOpsMethods) {
                        try {
                            if(phoneDetails.getFirstSimMCC() == -1) {
                                String operatorInfoSIM1 = (String) TypeReflection.invokeMethod(firstSimClassObject, operatorMethod, 1, int.class);
                                if (operatorInfoSIM1 != null) {
                                    if (!operatorInfoSIM1.isEmpty()) {
                                        int mccSIM1 = Integer.parseInt(operatorInfoSIM1.substring(0, 3));
                                        int mncSIM1 = Integer.parseInt(operatorInfoSIM1.substring(3));
                                        phoneDetails.setFirstSimMCC(mccSIM1);
                                        phoneDetails.setFirstSimMNC(mncSIM1);
                                    }
                                }
                            }

                            String operatorInfoSIM2 = (String) TypeReflection.invokeMethod(secondSimClassObject, operatorMethod, 2, int.class);
                            if (operatorInfoSIM2 != null) {
                                if (!operatorInfoSIM2.isEmpty()) {
                                    int mccSIM2 = Integer.parseInt(operatorInfoSIM2.substring(0, 3));
                                    int mncSIM2 = Integer.parseInt(operatorInfoSIM2.substring(3));
                                    phoneDetails.setSecondSimMCC(mccSIM2);
                                    phoneDetails.setSecondSimMNC(mncSIM2);
                                }
                            }
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                if(phoneDetails.getSecondSimMCC() == -1) {
                    for (String operatorMethod : simOpsMethods) {
                        try {
                            if(phoneDetails.getFirstSimMCC() == -1) {
                                String operatorInfoSIM1 = (String) TypeReflection.invokeMethod(firstSimClassObject, operatorMethod, 0 ,long.class);
                                if (operatorInfoSIM1 != null) {
                                    if (!operatorInfoSIM1.isEmpty()) {
                                        int mccSIM1 = Integer.parseInt(operatorInfoSIM1.substring(0, 3));
                                        int mncSIM1 = Integer.parseInt(operatorInfoSIM1.substring(3));
                                        phoneDetails.setFirstSimMCC(mccSIM1);
                                        phoneDetails.setFirstSimMNC(mncSIM1);
                                    }
                                }
                            }

                            String operatorInfoSIM2 = (String) TypeReflection.invokeMethod(secondSimClassObject, operatorMethod, 1, long.class);
                            if (operatorInfoSIM2 != null) {
                                if (!operatorInfoSIM2.isEmpty()) {
                                    int mccSIM2 = Integer.parseInt(operatorInfoSIM2.substring(0, 3));
                                    int mncSIM2 = Integer.parseInt(operatorInfoSIM2.substring(3));
                                    phoneDetails.setSecondSimMCC(mccSIM2);
                                    phoneDetails.setSecondSimMNC(mncSIM2);
                                }
                            }
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                if(phoneDetails.getSecondSimMCC() != -1) {
                    break;
                }
           } */
            setIsDualSim();
        }

        private void setIsDualSim() {
            try {
                if (!TextUtils.isEmpty(phoneDetails.getFirstSimID()) && !TextUtils.isEmpty(phoneDetails.getSecondSimID())) {
                    boolean isSame = phoneDetails.getFirstSimID().equalsIgnoreCase(phoneDetails.getSecondSimID());
                    phoneDetails.setIsDualSIM(!isSame);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            phoneDetails.setIsDualSIM(false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            this.setOnPhoneDetailsFetched.onFetched(phoneDetails);
        }
    }

}
