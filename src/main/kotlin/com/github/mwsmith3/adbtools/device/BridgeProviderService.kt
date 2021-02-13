package com.github.mwsmith3.adbtools.device

import com.android.ddmlib.AndroidDebugBridge
import com.google.common.util.concurrent.ListenableFuture
import com.intellij.openapi.project.Project

interface BridgeProviderService {

    fun getListenableFutureBridgeProvider(): (project: Project) -> ListenableFuture<AndroidDebugBridge>
}
