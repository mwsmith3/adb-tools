package com.github.mwsmith3.adbtools.device

import com.android.ddmlib.AndroidDebugBridge
import com.android.ddmlib.IDevice
import com.android.tools.idea.adb.AdbService
import com.android.tools.idea.run.ConnectedAndroidDevice
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.SettableFuture
import com.intellij.openapi.application.AppUIExecutor
import com.intellij.openapi.application.impl.coroutineDispatchingContext
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import kotlinx.coroutines.launch
import org.jetbrains.android.sdk.AndroidSdkUtils
import java.io.File
import kotlin.coroutines.CoroutineContext

class DeviceProviderServiceImpl(private val project: Project) : DeviceProviderService {

    override val coroutineContext: CoroutineContext
        get() = AppUIExecutor.onUiThread().coroutineDispatchingContext()

    private var adb: File? = null
    private var bridge: AndroidDebugBridge? = null
    private val listeners = mutableListOf<DeviceProviderServiceListener>()
    private val devices = mutableListOf<ConnectedAndroidDevice>()

    private val debugBridgeChangeListener = AndroidDebugBridge.IDebugBridgeChangeListener { changedDebugBridge ->
        Logger.getInstance(DeviceProviderServiceImpl::class.java).info("Device bridge changed: $changedDebugBridge")
        launch {
            if (bridge != null) {
                devices.clear()
                listeners.forEach {
                    it.serviceRestarted()
                }
                bridge = null
            }
            if (changedDebugBridge != null) {
                bridge = changedDebugBridge
                devices.clear()
                if (changedDebugBridge.hasInitialDeviceList()) {
                    devices.addAll(
                        changedDebugBridge.devices.map {
                            ConnectedAndroidDevice(it, null)
                        }
                    )
                }
            }
        }
    }

    private val iDeviceChangeListener = object : AndroidDebugBridge.IDeviceChangeListener {
        override fun deviceConnected(iDevice: IDevice) {
            Logger.getInstance(DeviceProviderServiceImpl::class.java).info("Device connected: ${iDevice.name}")
            launch {
                val device = ConnectedAndroidDevice(iDevice, null)
                devices.add(device)
                listeners.forEach { deviceChangeListener ->
                    deviceChangeListener.deviceAdded(device)
                }
            }
        }

        override fun deviceDisconnected(iDevice: IDevice) {
            Logger.getInstance(DeviceProviderServiceImpl::class.java).info("Device disconnected: ${iDevice.name}")
            launch {
                val device = findDevice(iDevice)
                if (device != null) {
                    listeners.forEach { listener ->
                        listener.deviceRemoved(device)
                    }
                }
            }
        }

        override fun deviceChanged(iDevice: IDevice, changeMask: Int) {
            Logger.getInstance(DeviceProviderServiceImpl::class.java).info("Device changed: ${iDevice.name}")
            launch {
                val device = findDevice(iDevice)
                if (device != null) {
                    listeners.forEach {
                        it.deviceChanged(device)
                    }
                }
            }
        }
    }

    override fun start(): ListenableFuture<AndroidDebugBridge> {
        val file = AndroidSdkUtils.getAdb(project)
        return if (file == null) {
            SettableFuture.create()
        } else {
            AndroidDebugBridge.addDebugBridgeChangeListener(debugBridgeChangeListener)
            AndroidDebugBridge.addDeviceChangeListener(iDeviceChangeListener)
            adb = file
            AdbService.getInstance().getDebugBridge(file)
        }
    }

    override fun addDeviceProviderServiceListener(listener: DeviceProviderServiceListener) {
        listeners.add(listener)
    }

    override fun removeDeviceProviderServiceListener(listener: DeviceProviderServiceListener) {
        listeners.remove(listener)
    }

    override fun getDevices(): List<ConnectedAndroidDevice> {
        return devices
    }

    override fun dispose() {
        AndroidDebugBridge.removeDeviceChangeListener(iDeviceChangeListener)
        AndroidDebugBridge.removeDebugBridgeChangeListener(debugBridgeChangeListener)
        devices.clear()
        bridge = null
    }

    private fun findDevice(iDevice: IDevice): ConnectedAndroidDevice? {
        return devices.firstOrNull { it.device == iDevice }
    }
}
