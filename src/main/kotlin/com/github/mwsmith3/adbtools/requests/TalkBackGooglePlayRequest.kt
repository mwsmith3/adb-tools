package com.github.mwsmith3.adbtools.requests

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.adb.GenericReceiver
import com.intellij.openapi.project.Project
import java.util.concurrent.TimeUnit

class TalkBackGooglePlayRequest : Request<Unit>() {
    override val command = "am start -a android.intent.action.VIEW -d 'market://details?id=com.google.android.marvin.talkback'"

    override val requestDescription: String
        get() = "Navigate to GooglePlay TalkBack install"

    override fun execute(project: Project, device: IDevice) {
        return device.executeShellCommand(command, GenericReceiver(), 15L, TimeUnit.SECONDS)
    }
}