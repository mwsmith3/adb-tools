package com.github.mwsmith3.adbtools.deeplinks

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.ReadonlyStatusHandler
import com.intellij.psi.xml.XmlFile
import org.jetbrains.android.dom.manifest.Manifest
import org.jetbrains.android.facet.AndroidFacet
import org.jetbrains.android.facet.AndroidRootUtil
import org.jetbrains.android.facet.SourceProviderManager
import org.jetbrains.android.util.AndroidUtils

object ManifestFinder{
    fun find(project: Project): List<Manifest> {
        val facets = AndroidUtils.getApplicationFacets(project)
        facets[0].module
        return facets.mapNotNull {
            val virtualFile = AndroidRootUtil.getPrimaryManifestFile(it)
            Logger.getInstance(ManifestFinder::class.java).info("virtual manifest file: ${virtualFile?.canonicalPath}")
            if (virtualFile == null || !ReadonlyStatusHandler.ensureFilesWritable(it.module.project, virtualFile)) {
                null
            } else {
                AndroidUtils.loadDomElement(it.module, virtualFile, Manifest::class.java)
            }
        }
    }

    fun findSingle(facet: AndroidFacet): Manifest? {
        val virtualFile = AndroidRootUtil.getPrimaryManifestFile(facet)
        Logger.getInstance(ManifestFinder::class.java).info("virtual manifest file: ${virtualFile?.canonicalPath}")
        return if (virtualFile == null || !ReadonlyStatusHandler.ensureFilesWritable(facet.module.project, virtualFile)) {
            null
        } else {
            AndroidUtils.loadDomElement(facet.module, virtualFile, Manifest::class.java)
        }
    }
}

//object ManifestFinder{
//    fun find(project: Project): List<Manifest> {
//        val facets = AndroidUtils.getApplicationFacets(project)
//        return facets.flatMap { facet ->
//            IdeaSourceProvider.getManifestFiles(facet).map { virtualFile ->
//                Pair(facet, virtualFile)
//            }
//        }.mapNotNull {
//            AndroidUtils.loadDomElement(it.first.module, it.second, Manifest::class.java)
//        }
//    }
//}
