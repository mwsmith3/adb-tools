package com.github.mwsmith3.adbtools.actions.adbexecutable

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.command.OpenDeepLinkCommand
import com.github.mwsmith3.adbtools.requests.CommandRunner
import com.github.mwsmith3.adbtools.window.DeepLinkData
import com.intellij.openapi.project.Project

class OpenDeepLinkExecutable(private val deepLinkData: DeepLinkData) : AdbExecutable {
    override fun execute(project: Project, device: IDevice) {
        CommandRunner.run(device, OpenDeepLinkCommand(project, deepLinkData))
    }
}