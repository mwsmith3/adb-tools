package com.github.mwsmith3.adbtools.command

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.adb.GenericReceiver
import com.github.mwsmith3.adbtools.deeplinks.DeepLinkData
import com.github.mwsmith3.adbtools.util.Debugger
import com.intellij.openapi.project.Project
import java.util.concurrent.TimeUnit

class OpenDeepLinkCommand(private val project: Project, private val packageName: String, private val deepLinkData: DeepLinkData) : Command<Unit> {
    private val deepLink = "\"${deepLinkData.deepLink.link}${deepLinkData.param}${deepLinkData.value}\""

    override val command: String
        get() = "am start -a android.intent.action.VIEW -d $deepLink $packageName"
    override val description: String
        get() = "Open deep link"

    override fun run(device: IDevice) {
        device.executeShellCommand(command, GenericReceiver(), 15L, TimeUnit.SECONDS)
        if (deepLinkData.attachDebugger) {
            Debugger(project, device, packageName).attach()
        }
    }
}