package com.codetagging.simflect.util

import android.content.Context
import com.codetagging.simflect.models.MethodProperties
import com.codetagging.simflect.models.MethodType
import com.codetagging.simflect.models.TelephonyProvider

object Constants {

    val DEFAULT_CLASS = "android.telephony.TelephonyManager"
    val SAMSUNG_CLASS = "android.telephony.MultiSimTelephonyManager"
    val MEDIATEK_CLASS = "com.mediatek.telephony.TelephonyManagerEx"

    val GENERIC_METHODS: List<MethodProperties> by lazy {
        listOf(
                MethodProperties("getDeviceId", MethodType.DEVICE_ID, null, null),
                MethodProperties("getDeviceId", MethodType.DEVICE_ID, Int::class.javaPrimitiveType, 1),
                MethodProperties("getDeviceIdGemini", MethodType.DEVICE_ID, Int::class.javaPrimitiveType, 1),
                MethodProperties("getNetworkOperatorName", MethodType.OPERATOR_NAME, null, null),
                MethodProperties("getNetworkOperatorName", MethodType.OPERATOR_NAME, Int::class.javaPrimitiveType, 1),
                MethodProperties("getNetworkOperatorName", MethodType.OPERATOR_NAME, Long::class.javaPrimitiveType, 2),
                MethodProperties("getNetworkOperatorNameGemini", MethodType.OPERATOR_NAME, Int::class.javaPrimitiveType, 1),
                MethodProperties("getSimOperatorName", MethodType.OPERATOR_NAME, null, null),
                MethodProperties("getSimOperatorName", MethodType.OPERATOR_NAME, Int::class.javaPrimitiveType, 1),
                MethodProperties("getSimOperatorName", MethodType.OPERATOR_NAME, Long::class.javaPrimitiveType, 2),
                MethodProperties("getSimOperatorNameGemini", MethodType.OPERATOR_NAME, Int::class.javaPrimitiveType, 1),
                MethodProperties("getNetworkOperator", MethodType.OPERATOR_DETAIL, null, null),
                MethodProperties("getNetworkOperator", MethodType.OPERATOR_DETAIL, Int::class.javaPrimitiveType, 1),
                MethodProperties("getNetworkOperator", MethodType.OPERATOR_DETAIL, Long::class.javaPrimitiveType, 2),
                MethodProperties("getNetworkOperatorGemini", MethodType.OPERATOR_DETAIL, Int::class.javaPrimitiveType, 1),
                MethodProperties("getSimOperator", MethodType.OPERATOR_DETAIL, null, null),
                MethodProperties("getSimOperator", MethodType.OPERATOR_DETAIL, Int::class.javaPrimitiveType, 1),
                MethodProperties("getSimOperator", MethodType.OPERATOR_DETAIL, Long::class.javaPrimitiveType, 2),
                MethodProperties("getSimOperatorGemini", MethodType.OPERATOR_DETAIL, Int::class.javaPrimitiveType, 1),
                MethodProperties("getSimState", MethodType.SIM_STATE, null, null),
                MethodProperties("getSimState", MethodType.SIM_STATE, Int::class.javaPrimitiveType, 1),
                MethodProperties("getSimState", MethodType.SIM_STATE, Long::class.javaPrimitiveType, 2),
                MethodProperties("getSimStateGemini", MethodType.SIM_STATE, Int::class.javaPrimitiveType, 1),
                MethodProperties("getSubscriberInfo", MethodType.SUBSCRIPTION_INFO, null, null),
                MethodProperties("getSubscriberInfo", MethodType.SUBSCRIPTION_INFO, Int::class.javaPrimitiveType, 1)
        )
    }

    val TELEPHONY_PROVIDERS: List<TelephonyProvider> by lazy {
        listOf(
                TelephonyProvider("default", DEFAULT_CLASS, "getDefault", null, null),
                TelephonyProvider("default", DEFAULT_CLASS, "getDefault", Int::class.javaPrimitiveType, 1),
                TelephonyProvider("default", MEDIATEK_CLASS, "TelephonyManagerEx", Context::class.java, null),
                TelephonyProvider("asus", DEFAULT_CLASS, "get2ndTm", null, null),
                TelephonyProvider("asus", DEFAULT_CLASS, "getTmBySlot", Int::class.javaPrimitiveType, 1),
                TelephonyProvider("samsung", SAMSUNG_CLASS, "getDefault", Int::class.javaPrimitiveType, 1),
                TelephonyProvider("samsung", DEFAULT_CLASS, "getSecondary", null, null)
        )
    }
}
