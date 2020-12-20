package com.github.mwsmith3.adbtools.deeplinks

import com.github.mwsmith3.adbtools.util.GetAndroidStrings
import org.jetbrains.android.dom.AndroidDomUtil
import org.jetbrains.android.dom.manifest.Data
import org.jetbrains.android.dom.manifest.Manifest
import org.jetbrains.android.facet.AndroidFacet
import com.intellij.openapi.diagnostic.Logger
import org.jetbrains.android.util.AndroidResourceUtil

object DeepLinkParser {

    fun getDeepLinks(facet: AndroidFacet): List<DeepLink> {
        val manifest = ManifestFinder.findSingle(facet) ?: return emptyList()

        val activities = manifest.application.activities
        val intentFilters = activities.flatMap {
            it.intentFilters
        }
        val deepLinkIntentFilters = intentFilters.filter {
            AndroidDomUtil.containsAction(it, "android.intent.action.VIEW") &&
                    AndroidDomUtil.containsCategory(it, "android.intent.category.DEFAULT") &&
                    AndroidDomUtil.containsCategory(it, "android.intent.category.BROWSABLE")
        }
        return deepLinkIntentFilters.flatMap {
            getDeepLinksFromData(manifest, facet, it.dataElements)
        }
    }

    private fun getDeepLinksFromData(manifest: Manifest, facet: AndroidFacet, data: List<Data>): List<DeepLink> {
        // TODO it should work according to this https://developer.android.com/guide/topics/manifest/data-element
        val schemes = getSchemes(manifest, facet, data)
        val hosts = getHosts(manifest, facet, data)
        val paths = getPaths(manifest, facet, data)

        Logger.getInstance(GetAndroidStrings::class.java).info("getDeepLinksFromData schemes: $schemes")
        Logger.getInstance(GetAndroidStrings::class.java).info("getDeepLinksFromData hosts: $hosts")
        Logger.getInstance(GetAndroidStrings::class.java).info("getDeepLinksFromData paths: $paths")

        val links = mutableListOf<DeepLink>()
        schemes.forEach { scheme ->
            hosts.forEach { host ->
                paths.forEach { path ->
                    links.add(DeepLink(scheme, host, path))
                }
            }
        }
        return links
    }

    private fun getSchemes(manifest: Manifest, facet: AndroidFacet, data: List<Data>): List<String> {
        val attributeValues = getXmlAttributeValuesWithName("android:scheme", data = data)
        return attributeValues.mapNotNull {
            GetAndroidStrings.resolveAttribute(manifest, facet, it)
        }
    }

    private fun getHosts(manifest: Manifest, facet: AndroidFacet, data: List<Data>): List<String> {
        val attributeValues = getXmlAttributeValuesWithName("android:host", data = data)
        return attributeValues.mapNotNull {
            GetAndroidStrings.resolveAttribute(manifest, facet, it)
        }
    }

    private fun getPaths(manifest: Manifest, facet: AndroidFacet, data: List<Data>): List<String> {
        // TODO pathPrefix and the other thing
        val attributeValues = getXmlAttributeValuesWithName("android:path", "android:pathPattern", "android:pathPrefix", data = data)
        return attributeValues.mapNotNull {
            GetAndroidStrings.resolveAttribute(manifest, facet, it)
        }
    }

    private fun getXmlAttributeValuesWithName(vararg names: String, data: List<Data>): List<String> {
        return data.flatMap {
            it.xmlTag?.attributes?.toList()?.filter { attribute -> names.contains(attribute.name) }.orEmpty().mapNotNull { attribute -> attribute.value }
        }
    }
}

data class DeepLink(val scheme: String, val host: String, val path: String?) {
    val link = "${scheme}://${host}${path ?: ""}"
}