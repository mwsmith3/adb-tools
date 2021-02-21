package com.github.mwsmith3.adbtools.command.settings

import com.github.mwsmith3.adbtools.command.NoResultCommand

class ChangeDisplaySettingsCommand(private val displaySetting: Display) : NoResultCommand() {
    override val adbCommand: String
        get() = when (displaySetting) {
            is Display.Spec -> "wm size ${displaySetting.x}x${displaySetting.y} && wm density ${displaySetting.density}"
            is Display.Reset -> "wm size reset && wm density reset"
        }

    override val description: String
        get() = "Change display of device"
}
