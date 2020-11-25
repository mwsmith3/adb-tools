package com.github.mwsmith3.adbtools.command.settings

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.adb.GenericReceiver
import com.github.mwsmith3.adbtools.command.Command
import java.util.concurrent.TimeUnit

object GetTalkBackEnabledCommand : Command<Boolean> {
    override val command: String
        get() = "settings get secure enabled_accessibility_services"
    override val description: String
        get() = "Get TalkBack enabled"
    private const val talkBackSetting = "com.google.android.marvin.talkback/com.google.android.marvin.talkback.TalkBackService"

    override fun run(device: IDevice): Boolean {
        val receiver = GenericReceiver()
        device.executeShellCommand(command, receiver, 15L, TimeUnit.SECONDS)
        return receiver.adbOutputLines[0] == talkBackSetting
    }
}