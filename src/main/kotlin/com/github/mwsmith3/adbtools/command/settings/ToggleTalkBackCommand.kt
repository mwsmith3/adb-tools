package com.github.mwsmith3.adbtools.command.settings

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.adb.GenericReceiver
import com.github.mwsmith3.adbtools.command.Command
import com.github.mwsmith3.adbtools.command.Command.Companion.TIMEOUT
import java.util.concurrent.TimeUnit

class ToggleTalkBackCommand(private val enabled: Boolean) : Command<Unit> {

    private val putTalkBackCommand = "settings put secure enabled_accessibility_services "
    private val talkBackOffSetting = "com.android.talkback/com.google.android.marvin.talkback.TalkBackService"
    private val talkBackOnSetting =
        "com.google.android.marvin.talkback/com.google.android.marvin.talkback.TalkBackService"

    override val command: String
        get() = if (enabled) {
            putTalkBackCommand + talkBackOffSetting
        } else {
            putTalkBackCommand + talkBackOnSetting
        }

    override val description: String
        get() = "Toggle TalkBack status"

    override fun run(device: IDevice) {
        device.executeShellCommand(command, GenericReceiver(), TIMEOUT, TimeUnit.SECONDS)
    }
}
