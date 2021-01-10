package com.github.mwsmith3.adbtools.device

import com.android.ddmlib.AndroidDebugBridge
import com.android.tools.idea.run.ConnectedAndroidDevice
import com.google.common.util.concurrent.ListenableFuture
import com.intellij.openapi.Disposable
import kotlinx.coroutines.CoroutineScope

interface DeviceProviderService : Disposable, CoroutineScope {
    fun start(): ListenableFuture<AndroidDebugBridge>
    fun addDeviceProviderServiceListener(listener: DeviceProviderServiceListener)
    fun removeDeviceProviderServiceListener(listener: DeviceProviderServiceListener)
    fun getDevices(): List<ConnectedAndroidDevice>
}
