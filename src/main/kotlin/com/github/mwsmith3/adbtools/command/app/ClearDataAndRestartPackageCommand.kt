package com.github.mwsmith3.adbtools.command.app

import com.github.mwsmith3.adbtools.command.StartForResultCommand

class ClearDataAndRestartPackageCommand(packageName: String, private val activityName: String) :
    StartForResultCommand(packageName) {
    override val command: String
        get() = "pm clear $packageName && am start $packageName/$activityName"
    override val description: String
        get() = "Clear app data and restart"
}
