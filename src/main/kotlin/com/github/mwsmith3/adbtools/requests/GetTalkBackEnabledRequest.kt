package com.github.mwsmith3.adbtools.requests

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.adb.GenericReceiver
import com.intellij.openapi.project.Project
import java.util.concurrent.TimeUnit

class GetTalkBackEnabledRequest : Request<Boolean>() {
    override val requestDescription = "Get TalkBack enabled"
    override val command = "settings get secure enabled_accessibility_services"
    private val talkBackSetting = "com.google.android.marvin.talkback/com.google.android.marvin.talkback.TalkBackService"

    override fun execute(project: Project, device: IDevice): Boolean {
        val receiver = GenericReceiver()
        device.executeShellCommand(command, receiver, 15L, TimeUnit.SECONDS)
        return receiver.adbOutputLines[0] == talkBackSetting
    }
}