package com.github.mwsmith3.adbtools.deeplinks

import com.github.mwsmith3.adbtools.util.GetAndroidStrings
import org.jetbrains.android.dom.AndroidDomUtil
import org.jetbrains.android.dom.manifest.Data
import org.jetbrains.android.dom.manifest.Manifest
import org.jetbrains.android.facet.AndroidFacet

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
            getDeepLinksFromData(manifest, it.dataElements)
        }
    }

    private fun getDeepLinksFromData(manifest: Manifest, data: List<Data>): List<DeepLink> {
        // TODO it should work according to this https://developer.android.com/guide/topics/manifest/data-element
        val schemes = getSchemes(manifest, data)
        val hosts = getHosts(manifest, data)
        val paths = getPaths(manifest, data)
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

    private fun getSchemes(manifest: Manifest, data: List<Data>): List<String> {
        val attributeValues = getXmlAttributeValuesWithName("android:scheme", data)
        return attributeValues.flatMap {
            it.resolveResource(manifest)
        }
    }


    private fun getHosts(manifest: Manifest, data: List<Data>): List<String> {
        val attributeValues = getXmlAttributeValuesWithName("android:host", data)
        return attributeValues.flatMap {
            it.resolveResource(manifest)
        }
    }

    private fun getPaths(manifest: Manifest, data: List<Data>): List<String> {
        // TODO pathPrefix and the other thing
        val attributeValues = getXmlAttributeValuesWithName("android.path", data)
        return attributeValues.flatMap {
            it.resolveResource(manifest)
        }

    }

    private fun getXmlAttributeValuesWithName(name: String, data: List<Data>): List<String> {
        return data.flatMap {
            it.xmlTag?.attributes?.toList()?.filter { attribute -> attribute.name == name }.orEmpty().mapNotNull { attribute -> attribute.value }
        }
    }
}

fun String.resolveResource(manifest: Manifest): List<String> {
    // TODO use AndroidDomUtil.getResourceReferenceConverter()
//    val r = AndroidDomUtil.getResourceReferenceConverter()
//    r.
//    AndroidDomUtil.getAndroidResourceReference()
    return if (this.startsWith("@string/")) {
        GetAndroidStrings.getValues(manifest, this)
    } else {
        listOf(this)
    }
}

data class DeepLink(val scheme: String, val host: String, val path: String?) {
    val link = "${scheme}://${host}${path ?: ""}"
}

data class DeepLinkDataAttributes(val facet: AndroidFacet, val packageName: String, val data: List<Data>)
data class DeepLinkData(val deepLink: DeepLink, val attachDebugger: Boolean, val param: String, val value: String)