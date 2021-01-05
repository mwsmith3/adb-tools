package com.github.mwsmith3.adbtools.command

import com.intellij.openapi.project.Project

class OpenDeepLinkCommand(
    packageName: String,
    private val deepLink: String
) :
    StartForResultCommand(packageName) {
    override val command: String
        get() = "am start -a android.intent.action.VIEW -d \"$deepLink\" $packageName"
    override val description: String
        get() = "Open deep link"
}