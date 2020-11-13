package com.github.mwsmith3.adbtools.requests

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.adb.GenericReceiver
import com.intellij.openapi.project.Project
import java.util.concurrent.TimeUnit

class GetPackageInstalledRequest(private val packageName: String) : Request<Boolean>() {
    override val command = "pm list packages $packageName"
    override val requestDescription = "TalkBack installed check"

    override fun execute(project: Project, device: IDevice): Boolean {
        val receiver = GenericReceiver()
        device.executeShellCommand("pm list packages $packageName", receiver, 15L, TimeUnit.SECONDS)
        return receiver.adbOutputLines.contains("package:$packageName")
    }
}