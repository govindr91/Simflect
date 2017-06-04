package com.codetagging.simflect

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.text.TextUtils
import com.codetagging.simflect.models.MethodType
import com.codetagging.simflect.models.PhoneDetails
import com.codetagging.simflect.util.Constants
import com.codetagging.simflect.util.TypeReflection
import io.reactivex.Single

object SimManager {

    fun getPhoneDetails(context: Context): Single<PhoneDetails> {
        return Single.fromCallable {
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            val version = Build.VERSION.SDK_INT
            val phoneDetails = PhoneDetails()
            phoneDetails.manufacturer = manufacturer
            phoneDetails.model = model
            phoneDetails.versionCode = version
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                configurePostLollipop(context, phoneDetails)
            } else {
                configurePreLollipop(context, phoneDetails)
            }
            phoneDetails
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private fun configurePostLollipop(context: Context, phoneDetails: PhoneDetails) {
        val subscriptionManager = context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
        val subscriptionInfoList = subscriptionManager.activeSubscriptionInfoList
        if (subscriptionInfoList != null) {
            //get sim 1 info
            var subscriptionInfo = subscriptionInfoList[0]
            phoneDetails.firstSimID = subscriptionInfo.iccId.toString()
            phoneDetails.isFirstSimReady = true
            phoneDetails.firstSimOperatorName = subscriptionInfo.displayName.toString()
            phoneDetails.firstSimMCC = subscriptionInfo.mcc
            phoneDetails.firstSimMNC = subscriptionInfo.mnc

            if (subscriptionInfoList.size > 1) {
                //get sim2 info
                subscriptionInfo = subscriptionInfoList[1]
                phoneDetails.secondSimID = subscriptionInfo.iccId.toString()
                phoneDetails.isSecondSimReady = true
                phoneDetails.secondSimOperatorName = subscriptionInfo.displayName.toString()
                phoneDetails.secondSimMCC = subscriptionInfo.mcc
                phoneDetails.secondSimMNC = subscriptionInfo.mnc
                phoneDetails.isDualSIM = true
            } else {
                phoneDetails.isDualSIM = false
            }
            return
        } else {
            configurePreLollipop(context, phoneDetails)
        }
    }

    private fun configurePreLollipop(context: Context, phoneDetails: PhoneDetails) {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        phoneDetails.firstSimID = telephonyManager.deviceId
        phoneDetails.isFirstSimReady = telephonyManager.simState == TelephonyManager.SIM_STATE_READY
        phoneDetails.firstSimOperatorName = telephonyManager.networkOperatorName
        try {
            val operatorDetail = telephonyManager.networkOperator
            val mcc = Integer.parseInt(operatorDetail.substring(0, 3))
            val mnc = Integer.parseInt(operatorDetail.substring(3))
            phoneDetails.firstSimMCC = mcc
            phoneDetails.firstSimMNC = mnc
        } catch (e: Exception) {
            e.printStackTrace()
        }

        var isIMEIFetched = false
        var isStateFetched = false
        var isOperatorNameFetched = false
        var isOperatorDetailFetched = false

        for ((_, className, classInstanceMethod, classInstanceParamDataType, classInstanceParamValue) in Constants.TELEPHONY_PROVIDERS) {
            var classObject: Any? = null
            if (TypeReflection.isClassAvailable(className)) {
                try {
                    classObject = TypeReflection.getClassStaticInstance(className,
                            classInstanceMethod, classInstanceParamValue,
                            classInstanceParamDataType)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                continue
            }

            var isFirstSimObjectHitPossible = false
            if (className!!.equals(Constants.DEFAULT_CLASS, ignoreCase = true) &&
                    classInstanceMethod!!.equals("getDefault", ignoreCase = true) &&
                    classInstanceParamDataType == null) {
                isFirstSimObjectHitPossible = true
            }

            for ((methodName, methodType, paramDataType, paramValue) in Constants.GENERIC_METHODS) {
                if (isFirstSimObjectHitPossible && paramDataType == null) {
                    continue
                }
                when (methodType) {
                    MethodType.DEVICE_ID -> {
                        if (!isIMEIFetched) {
                            try {
                                if (TypeReflection.isMethodAvailable(classObject, methodName, paramDataType)) {
                                    val imei = TypeReflection.invokeMethod(classObject, methodName,
                                            paramValue, paramDataType) as String
                                    if (!TextUtils.isEmpty(imei)) {
                                        phoneDetails.secondSimID = imei
                                        isIMEIFetched = true
                                    }
                                }
                            } catch (e: NoSuchMethodException) {
                                e.printStackTrace()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    }

                    MethodType.OPERATOR_NAME -> {
                        if (!isOperatorNameFetched) {
                            try {
                                if (TypeReflection.isMethodAvailable(classObject, methodName, paramDataType)) {
                                    val name = TypeReflection.invokeMethod(classObject, methodName,
                                            paramValue, paramDataType) as String
                                    if (!TextUtils.isEmpty(name)) {
                                        phoneDetails.secondSimOperatorName = name
                                        isOperatorNameFetched = true
                                    }
                                }
                            } catch (e: NoSuchMethodException) {
                                e.printStackTrace()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    }

                    MethodType.OPERATOR_DETAIL -> {
                        if (!isOperatorDetailFetched) {
                            try {
                                if (TypeReflection.isMethodAvailable(classObject, methodName, paramDataType)) {
                                    val detail = TypeReflection.invokeMethod(classObject, methodName,
                                            paramValue, paramDataType) as String
                                    if (!TextUtils.isEmpty(detail)) {
                                        val mcc = Integer.parseInt(detail.substring(0, 3))
                                        val mnc = Integer.parseInt(detail.substring(3))
                                        phoneDetails.secondSimMCC = mcc
                                        phoneDetails.secondSimMNC = mnc
                                        isOperatorDetailFetched = true
                                    }
                                }
                            } catch (e: NoSuchMethodException) {
                                e.printStackTrace()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    }

                    MethodType.SIM_STATE -> {
                        if (!isStateFetched) {
                            try {
                                if (TypeReflection.isMethodAvailable(classObject, methodName, paramDataType)) {
                                    val simState = TypeReflection.invokeMethod(classObject, methodName,
                                            paramValue, paramDataType) == 5 as Any
                                    phoneDetails.isSecondSimReady = simState
                                    isStateFetched = true
                                }
                            } catch (e: NoSuchMethodException) {
                                e.printStackTrace()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    }

                    MethodType.SUBSCRIPTION_INFO -> {
                        if (!isIMEIFetched) {
                            try {
                                if (TypeReflection.isMethodAvailable(classObject, methodName, paramDataType)) {
                                    val interfaceClass = TypeReflection.invokeMethod(classObject, methodName,
                                            paramValue, paramDataType) as Class<*>
                                    val deviceId = TypeReflection.invokeMethod(interfaceClass, "getDeviceId", null, null) as String
                                    if (!TextUtils.isEmpty(deviceId)) {
                                        phoneDetails.secondSimID = deviceId
                                        isIMEIFetched = true
                                    }
                                }
                            } catch (e: NoSuchMethodException) {
                                e.printStackTrace()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    }
                }
                if (isIMEIFetched && isOperatorDetailFetched && isOperatorNameFetched && isStateFetched) {
                    break
                }
            }
            if (isIMEIFetched && isOperatorDetailFetched && isOperatorNameFetched && isStateFetched) {
                break
            }
        }
        setIsDualSim(phoneDetails)
    }

    private fun setIsDualSim(phoneDetails: PhoneDetails) {
        try {
            if (!TextUtils.isEmpty(phoneDetails.firstSimID) && !TextUtils.isEmpty(phoneDetails.secondSimID)) {
                val isSame = phoneDetails.firstSimID!!.equals(phoneDetails.secondSimID!!, ignoreCase = true)
                phoneDetails.isDualSIM = !isSame
            }
        } catch (e: Exception) {
            phoneDetails.isDualSIM = false
            e.printStackTrace()
        }
    }

}
