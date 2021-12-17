package com.shimmer.lib_base.event

import org.greenrobot.eventbus.EventBus

object EventManager {

    fun register(subscriber: Any) {
        EventBus.getDefault().register(subscriber)
    }

    fun unRegister(subscriber: Any) {
        EventBus.getDefault().unregister(subscriber)
    }

    private fun post(event: MessageEvent) {
        EventBus.getDefault().post(event)
    }

    fun post(type: Int) {
        post(MessageEvent(type = type))
    }

    fun post(type: Int, string: String) {
        post(MessageEvent(type = type, stringValue = string))
    }

    fun post(type: Int, int: Int) {
        post(MessageEvent(type = type, intValue = int))
    }

    fun post(type: Int, boolean: Boolean) {
        post(MessageEvent(type = type, booleanValue = boolean))
    }
}