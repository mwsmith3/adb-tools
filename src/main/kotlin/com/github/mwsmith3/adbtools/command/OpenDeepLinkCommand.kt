package com.github.mwsmith3.adbtools.command

import com.intellij.openapi.project.Project

class OpenDeepLinkCommand(
    attachDebugger: Boolean,
    packageName: String,
    project: Project,
    private val deepLink: String
) :
    StartForResultCommand(attachDebugger, packageName, project) {
    override val command: String
        get() = "am start -a android.intent.action.VIEW -d \"$deepLink\" $packageName"
    override val description: String
        get() = "Open deep link"
}