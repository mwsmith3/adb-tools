package com.github.mwsmith3.adbtools.requests

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.adb.AdbUtil
import com.intellij.openapi.project.Project

class GetTalkBackInstalledRequest : Request<Boolean>() {
    private val talkBackPackage = "com.google.android.marvin.talkback"
    override val requestDescription: String
        get() = "TalkBack installed check"

    override fun execute(project: Project, device: IDevice): Boolean {
        return AdbUtil.isAppInstalled(device, talkBackPackage)
    }
}