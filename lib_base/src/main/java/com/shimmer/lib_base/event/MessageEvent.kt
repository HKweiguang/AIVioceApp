package com.shimmer.lib_base.event

data class MessageEvent(
    val type: Int,
    var stringValue:String = "",
    var intValue: Int = 0,
    var booleanValue: Boolean = false
)