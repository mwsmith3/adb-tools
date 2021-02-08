package com.github.mwsmith3.adbtools.actions.app

import com.android.ddmlib.InstallException
import com.github.mwsmith3.adbtools.actions.AdbAction
import com.github.mwsmith3.adbtools.util.NotificationHelper
import com.intellij.openapi.actionSystem.AnActionEvent

class UninstallAction : AdbAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val device = getDevice(event)
        val project = event.project
        val packageName = getPackageName(event)

        if (project != null && device != null && packageName != null) {
            execute(
                Runnable {
                    try {
                        val errorCode = device.uninstallPackage(packageName)
                        if (errorCode == null) {
                            NotificationHelper.info("$packageName uninstalled on ${device.name}")
                        } else {
                            NotificationHelper.error("$packageName is not installed on ${device.name}")
                        }
                    } catch (e: InstallException) {
                        NotificationHelper.error(
                            "Uninstalled failure for $packageName, on ${device.name}: ${e.message}"
                        )
                    }
                }
            )
        }
    }
}
