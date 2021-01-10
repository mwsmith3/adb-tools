package com.github.mwsmith3.adbtools.device

import com.android.tools.idea.run.ConnectedAndroidDevice

interface DeviceProviderServiceListener {
    fun deviceAdded(device: ConnectedAndroidDevice)
    fun deviceRemoved(device: ConnectedAndroidDevice)
    fun deviceChanged(device: ConnectedAndroidDevice)
    fun serviceRestarted()
}
