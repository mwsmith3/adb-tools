package com.github.mwsmith3.adbtools.deeplinks

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.vfs.ReadonlyStatusHandler
import org.jetbrains.android.dom.manifest.Manifest
import org.jetbrains.android.facet.AndroidFacet
import org.jetbrains.android.facet.AndroidRootUtil
import org.jetbrains.android.util.AndroidUtils

object ManifestFinder {

    fun find(facet: AndroidFacet): Manifest? {
        val virtualFile = AndroidRootUtil.getPrimaryManifestFile(facet)
        Logger.getInstance(ManifestFinder::class.java).info("virtual manifest file: ${virtualFile?.canonicalPath}")
        return if (virtualFile == null ||
            !ReadonlyStatusHandler.ensureFilesWritable(facet.module.project, virtualFile)
        ) {
            null
        } else {
            AndroidUtils.loadDomElement(facet.module, virtualFile, Manifest::class.java)
        }
    }
}
