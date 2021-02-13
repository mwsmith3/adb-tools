package com.github.mwsmith3.adbtools.device

import com.android.ddmlib.AndroidDebugBridge
import com.google.common.util.concurrent.ListenableFuture
import com.intellij.openapi.project.Project

class BridgeProviderServiceFake : BridgeProviderService {

    var provider: ((project: Project) -> ListenableFuture<AndroidDebugBridge>)? = null

    override fun getListenableFutureBridgeProvider(): (project: Project) -> ListenableFuture<AndroidDebugBridge> {
        return provider ?: throw RuntimeException("listener provider not set")
    }
}
