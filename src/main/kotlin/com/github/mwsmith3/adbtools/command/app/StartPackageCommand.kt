package com.github.mwsmith3.adbtools.command.app

import com.github.mwsmith3.adbtools.command.DebuggableCommand
import com.intellij.openapi.project.Project

class StartPackageCommand(attachDebugger: Boolean, packageName: String, activityName: String, project: Project) :
        DebuggableCommand(attachDebugger, packageName, activityName, project) {
    override val command: String
        get() = "am start $packageName/$activityName"
    override val description: String
        get() = "Start activity"
}