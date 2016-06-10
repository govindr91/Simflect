package com.codetagging.simflect.builder;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.List;

import com.codetagging.simflect.SimManager;
import com.codetagging.simflect.models.MethodProperties;
import com.codetagging.simflect.models.PhoneDetails;
import com.codetagging.simflect.models.TelephonyProvider;
import com.codetagging.simflect.util.Constants;

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

                for (MethodProperties methodProperties : TelephonyProviderBuilder.getGenericMethods()) {
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
