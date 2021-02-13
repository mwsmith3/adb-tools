package com.github.mwsmith3.adbtools.device

import com.android.ddmlib.AndroidDebugBridge
import com.android.tools.idea.adb.AdbService
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.intellij.openapi.project.Project
import org.jetbrains.android.sdk.AndroidSdkUtils
import java.io.FileNotFoundException

class BridgeProviderServiceImpl : BridgeProviderService {

    override fun getListenableFutureBridgeProvider(): (project: Project) -> ListenableFuture<AndroidDebugBridge> {
        return { project ->
            val file = AndroidSdkUtils.getAdb(project)
            if (file == null) {
                Futures.immediateFailedFuture(FileNotFoundException("Android debug bridge not found"))
            } else {
                AdbService.getInstance().getDebugBridge(file)
            }
        }
    }
}
