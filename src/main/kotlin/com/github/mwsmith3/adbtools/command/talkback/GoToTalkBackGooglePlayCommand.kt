package com.github.mwsmith3.adbtools.command.talkback

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.adb.GenericReceiver
import com.github.mwsmith3.adbtools.command.Command
import java.util.concurrent.TimeUnit

object GoToTalkBackGooglePlayCommand : Command<Unit> {
    override val command: String
        get() = "am start -a android.intent.action.VIEW -d 'market://details?id=com.google.android.marvin.talkback'"
    override val description: String
        get() = "Navigate to GooglePlay TalkBack install"

    override fun run(device: IDevice) {
        device.executeShellCommand(command, GenericReceiver(), 15L, TimeUnit.SECONDS)
    }
}