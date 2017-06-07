package com.codetagging.simflect.models

data class TelephonyProvider(
        var manufacturer: String,
        var className: String,
        var classInstanceMethod: String,
        var classInstanceParamDataType: Class<*>? = null,
        var classInstanceParamValue: Any? = null
)
