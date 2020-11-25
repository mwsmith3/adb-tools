package com.github.mwsmith3.adbtools.command.settings

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.adb.GenericReceiver
import com.github.mwsmith3.adbtools.command.Command
import java.util.concurrent.TimeUnit

class ChangeDisplaySettingsCommand(private val displaySetting: Display) : Command<Unit> {
    override val command: String
        get() = when (displaySetting) {
            is Display.Spec -> "wm size ${displaySetting.x}x${displaySetting.y} && wm density ${displaySetting.density}"
            is Display.Reset -> "wm size reset && wm density reset"
        }

    override val description: String
        get() = "Change display of device"

    override fun run(device: IDevice) {
        device.executeShellCommand(command, GenericReceiver(), 15L, TimeUnit.SECONDS)
    }
}
