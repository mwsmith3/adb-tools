package com.github.mwsmith3.adbtools.adb

import com.android.ddmlib.AdbCommandRejectedException
import com.android.ddmlib.IDevice
import com.android.ddmlib.ShellCommandUnresponsiveException
import com.android.ddmlib.TimeoutException
import com.android.tools.idea.gradle.project.sync.GradleSyncState
import com.github.mwsmith3.adbtools.util.NotificationHelper.info
import com.intellij.openapi.project.Project
import java.io.IOException
import java.util.concurrent.TimeUnit

object AdbUtil {
    // TODO print adb commands to log

    @Throws(TimeoutException::class, AdbCommandRejectedException::class, ShellCommandUnresponsiveException::class, IOException::class)
    fun isAppInstalled(device: IDevice, packageName: String): Boolean {
        val receiver = GenericReceiver()
        device.executeShellCommand("pm list packages $packageName", receiver, 15L, TimeUnit.SECONDS)
        return receiver.adbOutputLines.contains("package:$packageName")
    }

    fun isGradleSyncInProgress(project: Project): Boolean {
        return try {
            GradleSyncState.getInstance(project).isSyncInProgress
        } catch (t: Throwable) {
            info("Couldn't determine if a gradle sync is in progress")
            false
        }
    }
}