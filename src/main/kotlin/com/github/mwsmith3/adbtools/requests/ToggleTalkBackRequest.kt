package com.github.mwsmith3.adbtools.requests

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.adb.GenericReceiver
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import java.util.concurrent.TimeUnit

class ToggleTalkBackRequest : Request<Unit>() {
    override val requestDescription: String
        get() = "Toggle TalkBack status"

    private val talkBackSetting = "com.google.android.marvin.talkback/com.google.android.marvin.talkback.TalkBackService"
    private val getTalkBackSetting = "settings get secure enabled_accessibility_services"
    private val putTalkBackCommand = "settings put secure enabled_accessibility_services "
    private val talkBackOffSetting = "com.android.talkback/com.google.android.marvin.talkback.TalkBackService"
    private val talkBackOnSetting = "com.google.android.marvin.talkback/com.google.android.marvin.talkback.TalkBackService"

    override fun execute(project: Project, device: IDevice) {
        val receiver = GenericReceiver()
        device.executeShellCommand(getTalkBackSetting, receiver, 15L, TimeUnit.SECONDS)
        receiver.adbOutputLines.forEachIndexed { i, u ->
            Logger.getInstance(GetTalkBackInstalledRequest::class.java).info("Receiver line: $i, output: $u")
        }
        return if (receiver.adbOutputLines[0] == talkBackSetting) {
            device.executeShellCommand(putTalkBackCommand + talkBackOffSetting, GenericReceiver(), 15L, TimeUnit.SECONDS)
        } else {
            device.executeShellCommand(putTalkBackCommand + talkBackOnSetting, GenericReceiver(), 15L, TimeUnit.SECONDS)
        }
    }
}