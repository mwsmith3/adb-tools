package com.github.mwsmith3.adbtools.device

import com.android.ddmlib.IDevice
import com.intellij.openapi.application.Application
import com.intellij.util.messages.Topic

interface DeviceListener {
    fun onDevicesChanged(devices: List<IDevice>)

    companion object {
        val DEVICE_TOPIC = Topic.create("device topic", DeviceListener::class.java)
    }
}

fun Application.publishDeviceChanges(devices: List<IDevice>) {
    this.invokeLater {
        messageBus.syncPublisher(DeviceListener.DEVICE_TOPIC).onDevicesChanged(devices)
    }
}