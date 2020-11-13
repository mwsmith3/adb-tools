package com.github.mwsmith3.adbtools.command

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.adb.GenericReceiver
import java.util.concurrent.TimeUnit

class GetPackageInstalledCommand(private val packageName: String) : Command<Boolean> {
    override val command: String
        get() = "pm list packages $packageName"
    override val description: String
        get() = "TalkBack installed check"

    override fun run(device: IDevice): Boolean {
        val receiver = GenericReceiver()
        device.executeShellCommand("pm list packages $packageName", receiver, 15L, TimeUnit.SECONDS)
        return receiver.adbOutputLines.contains("package:$packageName")
    }
}