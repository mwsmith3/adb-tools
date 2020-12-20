package com.github.mwsmith3.adbtools.util

import com.android.ide.common.rendering.api.ResourceNamespace
import org.jetbrains.android.dom.converters.ResourceReferenceConverter
import com.android.resources.ResourceType
import com.android.tools.idea.res.ResourceRepositoryManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.util.xml.impl.ConvertContextFactory
import org.jetbrains.android.dom.manifest.Manifest
import org.jetbrains.android.facet.AndroidFacet

object GetAndroidStrings {

    fun resolveAttribute(manifest: Manifest, facet: AndroidFacet, attributeValue: String?): String? {
        return if (attributeValue != null && attributeValue.startsWith("@")) {
            Logger.getInstance(GetAndroidStrings::class.java).info("resolveAttribute attributeValue: $attributeValue")
            val resourceValue = lookUpStringResource(manifest, attributeValue, facet)
            Logger.getInstance(GetAndroidStrings::class.java).info("resolveAttribute resourceValue: $resourceValue")
            resolveAttribute(manifest, facet, resourceValue)
        } else {
            attributeValue
        }
    }

    private fun lookUpStringResource(manifest: Manifest, resourceReference: String, facet: AndroidFacet): String? {
        val resourceName = getStringResourceName(manifest, resourceReference)
        Logger.getInstance(GetAndroidStrings::class.java).info("lookUpStringResource resourceName: $resourceName")
        return resourceName?.let {
            ResourceRepositoryManager.getProjectResources(facet).getResources(ResourceNamespace.RES_AUTO, ResourceType.STRING)
                    .get(it)[0].resourceValue.rawXmlValue
        }
    }

    private fun getStringResourceName(manifest: Manifest, resourceReference: String): String? {
        val converter = ResourceReferenceConverter(ResourceType.STRING, true, true)
        val context = ConvertContextFactory.createConvertContext(manifest)
        val variants = converter.getVariants(context)
        val resourceValue = variants.firstOrNull {
            "$it" == resourceReference
        }
        Logger.getInstance(GetAndroidStrings::class.java).info("getStringResourceName variants: $variants")
        Logger.getInstance(GetAndroidStrings::class.java).info("getStringResourceName resourceRefernce: $resourceReference")
        Logger.getInstance(GetAndroidStrings::class.java).info("getStringResourceName getStringResourceValue: $resourceValue")
        return resourceValue?.resourceName
    }
}