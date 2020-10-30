package com.github.mwsmith3.adbtools.services

import com.android.ddmlib.AndroidDebugBridge
import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.ui.AdbToolWindow
import com.github.mwsmith3.adbtools.ui.AdbToolWindowPanel
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.content.ContentFactory

class DeviceProviderService(private val project: Project) {

    private val devices = DeviceContainer()

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
            val contentManager = contentManager()
            if (devices.isEmpty()) {
                contentManager?.removeAllContents(true)
            }
            devices.add(device)
            val adbToolWindowPanel = AdbToolWindowPanel(device)
            val deviceContent = contentFactory().createContent(adbToolWindowPanel, device.serialNumber, false)
            contentManager?.addContent(deviceContent)
        }
    }

    private fun removeDeviceWindow(device: IDevice) {
        ApplicationManager.getApplication().invokeLater {
            val contentManager = contentManager()
            val index = devices.remove(device)
            val removedDeviceContent = contentManager?.getContent(index)
            removedDeviceContent?.let {
                contentManager.removeContent(removedDeviceContent, true)
            }
            if (devices.isEmpty()) {
                val contentFactory = contentFactory()
                val emptyContent = contentFactory.createContent(AdbToolWindow.emptyContent, "", false)
                contentManager?.addContent(emptyContent)
            }
        }
    }

    fun tearDown() {
        AndroidDebugBridge.removeDeviceChangeListener(deviceChangeListener)
    }

    private fun contentFactory() = ContentFactory.SERVICE.getInstance()
    private fun contentManager() = ToolWindowManager.getInstance(project).getToolWindow("adb")?.contentManager
}