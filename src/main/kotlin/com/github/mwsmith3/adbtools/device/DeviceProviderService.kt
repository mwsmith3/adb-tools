package com.github.mwsmith3.adbtools.device

import com.android.ddmlib.AndroidDebugBridge
import com.android.ddmlib.IDevice
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener

class DeviceProviderService : Disposable {
    // TODO make disposable?
    private val _devices = mutableListOf<IDevice>()
    val devices: List<IDevice> = _devices

    private val deviceChangeListener = object : AndroidDebugBridge.IDeviceChangeListener {
        override fun deviceConnected(device: IDevice?) {
            Logger.getInstance(DeviceProviderService::class.java).info("DEVICE CONNECTED: device: ${device?.name} changed, state: ${device?.state}, avd name: ${device?.avdName}, offline: ${device?.isOffline}, online: ${device?.isOnline}")
            device?.let {
                _devices.add(it)
                ApplicationManager.getApplication().publishDeviceChanges(devices)
            }
        }

        override fun deviceDisconnected(device: IDevice?) {
            Logger.getInstance(DeviceProviderService::class.java).info("DEVICE DISCONNECTED: device: ${device?.name} changed, state: ${device?.state}, avd name: ${device?.avdName}, offline: ${device?.isOffline}, online: ${device?.isOnline}")
            device?.let {
                _devices.remove(it)
                ApplicationManager.getApplication().publishDeviceChanges(devices)
            }
        }

        override fun deviceChanged(device: IDevice?, changeMask: Int) {
            Logger.getInstance(DeviceProviderService::class.java).info("DEVICE CHANGED: device: ${device?.name} changed, state: ${device?.state}, avd name: ${device?.avdName}, offline: ${device?.isOffline}, online: ${device?.isOnline}")
        }
    }

    init {
        AndroidDebugBridge.addDeviceChangeListener(deviceChangeListener)
    }

    override fun dispose() {
        AndroidDebugBridge.removeDeviceChangeListener(deviceChangeListener)
    }
}