package com.github.mwsmith3.adbtools.util

import com.android.ide.common.rendering.api.ResourceNamespace
import com.android.resources.ResourceType
import com.android.tools.idea.res.ResourceRepositoryManager
import org.jetbrains.android.facet.AndroidFacet

object GetAndroidStrings {
    fun getValue(facet: AndroidFacet, key: String): String? {
        return ResourceRepositoryManager.getProjectResources(facet).getResources(ResourceNamespace.RES_AUTO, ResourceType.STRING)
                .get(key)[0].resourceValue.rawXmlValue
    }
}