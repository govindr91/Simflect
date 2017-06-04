package com.codetagging.simflect.models

data class PhoneDetails(
        var manufacturer: String? = null,
        var model: String? = null,
        var versionCode: Int = -1,
        var isDualSIM: Boolean = false,
        var isFirstSimReady: Boolean = false,
        var isSecondSimReady: Boolean = false,
        var firstSimID: String? = null,
        var secondSimID: String? = null,
        var firstSimOperatorName: String? = null,
        var secondSimOperatorName: String? = null,
        var firstSimMCC: Int = -1,
        var firstSimMNC: Int = -1,
        var secondSimMCC: Int = -1,
        var secondSimMNC: Int = -1
)