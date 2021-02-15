package com.github.mwsmith3.adbtools.deeplinks

import com.intellij.openapi.project.Project
import org.jetbrains.android.facet.AndroidFacet

class DeepLinkProviderServiceImpl(project: Project) : DeepLinkProviderService {

    override fun getDeepLinks(facet: AndroidFacet): List<String> {
        val dl = DeepLinkParser.getDeepLinks(facet)
        return dl.map { it.link }
    }
}
