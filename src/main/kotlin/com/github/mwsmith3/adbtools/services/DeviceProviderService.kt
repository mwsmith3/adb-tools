package com.github.mwsmith3.adbtools.services

import com.android.ddmlib.AndroidDebugBridge
import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.ui.AdbToolWindow
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.content.ContentFactory

class DeviceProviderService(private val project: Project) {

    private val deviceList = mutableListOf<IDevice>()

    private val deviceChangeListener = object : AndroidDebugBridge.IDeviceChangeListener {
        override fun deviceConnected(device: IDevice?) {
            Logger.getInstance(DeviceProviderService::class.java).info("DEVICE CONNECTED: device: ${device?.name} changed, state: ${device?.state}, avd name: ${device?.avdName}, offline: ${device?.isOffline}, online: ${device?.isOnline}")
            device?.let {
                addDeviceWindow(it)
            }
        }

        override fun deviceDisconnected(device: IDevice?) {
            Logger.getInstance(DeviceProviderService::class.java).info("DEVICE DISCONNECTED: device: ${device?.name} changed, state: ${device?.state}, avd name: ${device?.avdName}, offline: ${device?.isOffline}, online: ${device?.isOnline}")
            device?.let {
                removeDeviceWindow(it)
            }
        }

        override fun deviceChanged(device: IDevice?, changeMask: Int) {
            Logger.getInstance(DeviceProviderService::class.java).info("DEVICE CHANGED: device: ${device?.name} changed, state: ${device?.state}, avd name: ${device?.avdName}, offline: ${device?.isOffline}, online: ${device?.isOnline}")
        }
    }

    init {
        AndroidDebugBridge.addDeviceChangeListener(deviceChangeListener)
    }

    private fun addDeviceWindow(device: IDevice) {
        ApplicationManager.getApplication().invokeLater {
            val contentManager = ToolWindowManager.getInstance(project).getToolWindow("adb")?.contentManager
            val contentFactory = ContentFactory.SERVICE.getInstance()
            val deviceContent = contentFactory.createContent(AdbToolWindow.deviceContent, device.serialNumber, false)
            if (deviceList.isEmpty()) {
                contentManager?.removeAllContents(true)
            }
            deviceList.add(device)
            contentManager?.addContent(deviceContent)
        }
    }

    private fun removeDeviceWindow(device: IDevice) {
        ApplicationManager.getApplication().invokeLater {
            val index = deviceList.indexOf(device)
            deviceList.remove(device)
            val contentManager = ToolWindowManager.getInstance(project).getToolWindow("adb")?.contentManager
            val removedDeviceContent = contentManager?.getContent(index)
            removedDeviceContent?.let {
                contentManager.removeContent(removedDeviceContent, true)
            }
            if (deviceList.size == 0) {
                val contentFactory = ContentFactory.SERVICE.getInstance()
                val emptyContent = contentFactory.createContent(AdbToolWindow.emptyContent, "", false)
                contentManager?.addContent(emptyContent)
            }
        }
    }

    fun tearDown() {
        AndroidDebugBridge.removeDeviceChangeListener(deviceChangeListener)
    }
}