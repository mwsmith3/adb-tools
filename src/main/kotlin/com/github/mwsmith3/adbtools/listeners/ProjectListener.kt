package com.github.mwsmith3.adbtools.listeners

import com.github.mwsmith3.adbtools.services.DeviceProviderService
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener

internal class ProjectListener : ProjectManagerListener {

    override fun projectOpened(project: Project) {
        project.getService(DeviceProviderService::class.java)
    }

    override fun projectClosed(project: Project) {
        project.getService(DeviceProviderService::class.java).tearDown()
    }
}
