package com.github.mwsmith3.adbtools.command.app

import com.github.mwsmith3.adbtools.command.StartForResultCommand

class RestartPackageCommand(packageName: String, private val activityName: String) :
    StartForResultCommand(packageName) {
    override val adbCommand: String
        get() = "am force-stop $packageName && am start $packageName/$activityName"
    override val description: String
        get() = "Restart package"
}
