package com.github.mwsmith3.adbtools.requests

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.adb.GenericReceiver
import com.intellij.openapi.project.Project
import java.util.concurrent.TimeUnit

class ToggleTalkBackRequest(private val enabled: Boolean) : Request<Unit>() {

    private val putTalkBackCommand = "settings put secure enabled_accessibility_services "
    private val talkBackOffSetting = "com.android.talkback/com.google.android.marvin.talkback.TalkBackService"
    private val talkBackOnSetting = "com.google.android.marvin.talkback/com.google.android.marvin.talkback.TalkBackService"

    override val requestDescription: String
        get() = "Toggle TalkBack status"

    override val command: String
        get() = if (enabled) {
            putTalkBackCommand + talkBackOffSetting
        } else {
            putTalkBackCommand + talkBackOnSetting
        }

    override fun execute(project: Project, device: IDevice) {
        device.executeShellCommand(command, GenericReceiver(), 15L, TimeUnit.SECONDS)
    }
}