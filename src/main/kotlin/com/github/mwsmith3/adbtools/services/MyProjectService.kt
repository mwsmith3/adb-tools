package com.github.mwsmith3.adbtools.services

import com.intellij.openapi.project.Project
import com.github.mwsmith3.adbtools.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
