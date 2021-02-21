package com.github.mwsmith3.adbtools.command.app

import com.github.mwsmith3.adbtools.command.NoResultCommand

class ForceStopAppCommand(private val packageName: String) : NoResultCommand() {
    override val adbCommand: String
        get() = "am force-stop $packageName"
    override val description: String
        get() = "force stop app"
}
