package com.github.mwsmith3.adbtools.command.settings

import com.github.mwsmith3.adbtools.command.NoResultCommand

class EnableDeveloperOptionsCommand : NoResultCommand() {

    override val adbCommand: String
        get() = "settings put global development_settings_enabled 1"

    override val description: String
        get() = "Enable developer options"
}
