package com.github.mwsmith3.adbtools.actions.app

import com.android.ddmlib.InstallException
import com.github.mwsmith3.adbtools.actions.AdbAction
import com.github.mwsmith3.adbtools.util.Notifications
import com.intellij.openapi.actionSystem.AnActionEvent

class UninstallAction : AdbAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val device = getDevice(event)
        val project = event.project
        val packageName = getPackageName(event)

        if (project != null && device != null && packageName != null) {
            execute {
                try {
                    val errorCode = device.uninstallPackage(packageName)
                    if (errorCode == null) {
                        Notifications.info("$packageName uninstalled on ${device.name}", project)
                    } else {
                        Notifications.error("$packageName is not installed on ${device.name}", project)
                    }
                } catch (e: InstallException) {
                    Notifications.error(
                        "Uninstalled failure for $packageName, on ${device.name}: ${e.message}",
                        project
                    )
                }
            }
        }
    }
}
