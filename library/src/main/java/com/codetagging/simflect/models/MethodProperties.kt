package com.codetagging.simflect.models

data class MethodProperties(
        var methodName: String,
        var MethodType: MethodType,
        var paramDataType: Class<*>? = null,
        var paramValue: Any? = null
)
