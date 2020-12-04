package com.github.mwsmith3.adbtools.util

import org.jetbrains.android.dom.converters.ResourceReferenceConverter
import com.android.resources.ResourceType
import com.intellij.openapi.diagnostic.Logger
import com.intellij.util.xml.impl.ConvertContextFactory
import org.jetbrains.android.dom.manifest.Manifest

object GetAndroidStrings {
    fun getValues(manifest: Manifest, key: String): List<String> {
        val converter = ResourceReferenceConverter(ResourceType.STRING, true, true)
        val context = ConvertContextFactory.createConvertContext(manifest)
        val resourceValues = converter.getVariants(context)

        Logger.getInstance(GetAndroidStrings::class.java).info("$resourceValues ${resourceValues.map { it.value }} ${resourceValues.map { it.resourceName }} ${resourceValues.map { it.resourceType} } ${resourceValues.map { it.isReference} } ${resourceValues.map { it.`package`} }")

        return resourceValues.mapNotNull {
            if (it.resourceName == key) {
                it.value
            } else {
                null
            }
        }

//        return ResourceRepositoryManager.getProjectResources(facet).getResources(ResourceNamespace.RES_AUTO, ResourceType.STRING)
//                .get(key)[0].resourceValue.rawXmlValue
    }
}