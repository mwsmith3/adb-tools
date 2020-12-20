package com.github.mwsmith3.adbtools.command.app

import com.github.mwsmith3.adbtools.command.StartForResultCommand
import com.intellij.openapi.project.Project

class StartPackageCommand(attachDebugger: Boolean, packageName: String, project: Project, private val activityName: String) :
        StartForResultCommand(attachDebugger, packageName, project) {
    override val command: String
        get() = "am start $packageName/$activityName"
    override val description: String
        get() = "Start activity"
}