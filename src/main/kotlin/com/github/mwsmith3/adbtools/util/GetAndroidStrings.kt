package com.github.mwsmith3.adbtools.util

import com.android.ide.common.rendering.api.ResourceNamespace
import com.android.resources.ResourceType
import com.android.tools.idea.res.ResourceRepositoryManager
import com.github.mwsmith3.adbtools.window.AndroidString
import com.intellij.openapi.project.Project
import org.jetbrains.android.facet.AndroidFacet
import org.jetbrains.android.util.AndroidUtils

object GetAndroidStrings {
    fun get(project: Project): List<AndroidString> {
        val facets = AndroidUtils.getApplicationFacets(project)

        return facets.flatMap {
            ResourceRepositoryManager.getProjectResources(it).allResources.filter { resourceItem ->
                resourceItem.type == ResourceType.STRING
            }
        }.map { resourceItem ->
            AndroidString(resourceItem.key, resourceItem.resourceValue.rawXmlValue)
        }
    }

    fun getValue(facet: AndroidFacet, key: String): String? {
        return ResourceRepositoryManager.getProjectResources(facet).getResources(ResourceNamespace.RES_AUTO, ResourceType.STRING)
                .get(key)[0].resourceValue.rawXmlValue
    }
}