package com.github.mwsmith3.adbtools.device

import com.android.ddmlib.IDevice
import com.intellij.openapi.application.ApplicationManager
import com.intellij.util.messages.Topic

interface DeviceListener {
    fun onDevicesChanged(devices: List<IDevice>)

    companion object {
        private val DEVICE_TOPIC = Topic.create("device topic", DeviceListener::class.java)

        fun publish(devices: List<IDevice>) {
            val application = ApplicationManager.getApplication()
            application.invokeLater {
                application.messageBus.syncPublisher(DEVICE_TOPIC).onDevicesChanged(devices)
            }
        }
    }
}