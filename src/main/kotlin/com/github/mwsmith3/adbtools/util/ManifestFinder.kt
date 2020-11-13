package com.github.mwsmith3.adbtools.util

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.ReadonlyStatusHandler
import org.jetbrains.android.dom.manifest.Manifest
import org.jetbrains.android.facet.AndroidRootUtil
import org.jetbrains.android.util.AndroidUtils

object ManifestFinder{
    fun find(project: Project): List<Manifest> {
        val facets = AndroidUtils.getApplicationFacets(project)
        return facets.mapNotNull {
            val virtualFile = AndroidRootUtil.getPrimaryManifestFile(it)
            if (virtualFile == null || !ReadonlyStatusHandler.ensureFilesWritable(it.module.project, virtualFile)) {
                null
            } else {
                AndroidUtils.loadDomElement(it.module, virtualFile, Manifest::class.java)
            }
        }
    }
}