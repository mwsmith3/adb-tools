package com.github.mwsmith3.adbtools.actions.adbexecutable

import com.android.ddmlib.IDevice
import com.intellij.openapi.project.Project

interface AdbExecutable {
    fun execute(project: Project, device: IDevice)
}