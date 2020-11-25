package com.github.mwsmith3.adbtools.device

import com.android.ddmlib.AndroidDebugBridge
import com.android.ddmlib.IDevice
import com.android.tools.idea.run.ConnectedAndroidDevice
import com.intellij.openapi.Disposable
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project

class DeviceProviderService(private val project: Project) : Disposable {

    interface DeviceChangeListener {
        fun deviceAdded(device: ConnectedAndroidDevice)
        fun deviceRemoved(device: ConnectedAndroidDevice)
        fun deviceChanged(device: ConnectedAndroidDevice)
    }

    private val listeners = mutableListOf<DeviceChangeListener>()
    private val devices = mutableListOf<ConnectedAndroidDevice>()

    fun addListener(listener: DeviceChangeListener) {
        listeners.add(listener)
    }

    private val deviceChangeListener = object : AndroidDebugBridge.IDeviceChangeListener {
        override fun deviceConnected(iDevice: IDevice) {
            Logger.getInstance(DeviceProviderService::class.java).info("DEVICE CONNECTED: device: ${iDevice.name} changed, state: ${iDevice.state}, avd name: ${iDevice.avdName}, offline: ${iDevice.isOffline}, online: ${iDevice.isOnline}")
            val device = ConnectedAndroidDevice(iDevice, null)
            devices.add(device)
            listeners.forEach { deviceChangeListener ->
                deviceChangeListener.deviceAdded(device)
            }
        }

        override fun deviceDisconnected(iDevice: IDevice) {
            Logger.getInstance(DeviceProviderService::class.java).info("DEVICE DISCONNECTED: device: ${iDevice.name} changed, state: ${iDevice.state}, avd name: ${iDevice.avdName}, offline: ${iDevice.isOffline}, online: ${iDevice.isOnline}")
            val device = findDevice(iDevice)
            Logger.getInstance(DeviceProviderService::class.java).info("device found is $device")
            if (device != null) {
                listeners.forEach { listener ->
                    listener.deviceRemoved(device)
                }
            }
        }

        override fun deviceChanged(iDevice: IDevice, changeMask: Int) {
            Logger.getInstance(DeviceProviderService::class.java).info("DEVICE CHANGED: device: ${iDevice.name} changed, state: ${iDevice.state}, avd name: ${iDevice?.avdName}, offline: ${iDevice.isOffline}, online: ${iDevice.isOnline}")
            val device = findDevice(iDevice)
            if (device != null) {
                listeners.forEach {
                    it.deviceChanged(device)
                }
            }
        }
    }

    init {
        AndroidDebugBridge.addDeviceChangeListener(deviceChangeListener)
    }

    override fun dispose() {
        AndroidDebugBridge.removeDeviceChangeListener(deviceChangeListener)
    }

    private fun findDevice(iDevice: IDevice): ConnectedAndroidDevice? {
        return devices.firstOrNull { it.device == iDevice }
    }
}