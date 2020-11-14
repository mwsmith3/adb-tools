package com.github.mwsmith3.adbtools.util

import android.net.Uri
import com.android.ide.common.xml.AndroidManifestParser
import com.android.tools.idea.gradle.project.model.AndroidModuleModel
import com.android.tools.idea.util.androidFacet
import com.intellij.openapi.project.Project
import org.jetbrains.android.dom.AndroidDomUtil
import org.jetbrains.android.dom.manifest.Data
import org.jetbrains.android.facet.AndroidFacet

object DeepLinkParser {
    fun getDeepLinks(project: Project): List<DeepLink> {
        val manifests = ManifestFinder.find(project)
        val facetAndActivity = manifests.flatMap { manifest ->
            manifest.application.activities.mapNotNull { activity ->
                manifest.androidFacet?.let {  facet ->
                    Pair(facet, activity)
                }
            }
        }
        val intentFilters = facetAndActivity.flatMap { pair ->
            pair.second.intentFilters.map {
                Pair(pair.first, it)
            }
        }
        val deepLinkIntentFilters = intentFilters.filter { intentFilter ->
            AndroidDomUtil.containsAction(intentFilter.second, "android.intent.action.VIEW") &&
                    AndroidDomUtil.containsCategory(intentFilter.second, "android.intent.category.DEFAULT") &&
                    AndroidDomUtil.containsCategory(intentFilter.second, "android.intent.category.BROWSABLE")
        }
        val dataElements = deepLinkIntentFilters.flatMap { pair ->
            pair.second.dataElements.map {
                Pair(pair.first, it)
            }
        }
        return dataElements.flatMap { getDeepLinkFromData(it.first, it.second) }
    }

    private fun getDeepLinkFromData(facet: AndroidFacet, data: Data): List<DeepLink> {
        // TODO it should work according to this https://developer.android.com/guide/topics/manifest/data-element


        val packageName = facet.let {
            AndroidModuleModel.get(it)?.applicationId
        } ?: return emptyList()

        val attributes = data.xmlTag?.attributes?.toList().orEmpty()
        val schemes = attributes.filter {
            it.name == "android:scheme"
        }.mapNotNull {
            it.value?.resolveResource(facet)
        }

        val hosts = attributes.filter {
            it.name == "android:host"
        }.mapNotNull {
            it.value?.resolveResource(facet)
        }

        val paths = if (attributes.find { it.name == "android.path" } == null) {
            attributes.filter {
                it.name == "android:pathPrefix"
            }
        } else {
            attributes.filter {
                it.name == "android:path"
            }
        }.mapNotNull {
            it.value?.resolveResource(facet)
        }

        val links = mutableListOf<DeepLink>()
        schemes.forEach { scheme ->
            hosts.forEach { host ->
                paths.forEach { path ->
                    links += DeepLink(packageName, scheme, host, path)
                }
            }
        }
        return links

//        val scheme = attributes.find { it.name == "android:scheme" }?.value?.resolveResource(facet)
//
//
//        val host = attributes.find { it.name == "android:host" }?.value?.resolveResource(facet)
//
//
//        val pathPrefix = attributes.find { it.name == "android:pathPrefix" }?.value?.resolveResource(facet)
//
//
////        val packageName = facet.let {
////            AndroidModuleModel.get(it)?.applicationId
////        }
//        return if (packageName != null && scheme != null && host != null) {
//            DeepLink(packageName, scheme, host, pathPrefix)
//        } else {
//            null
//        }
    }
}

fun String.resolveResource(facet: AndroidFacet): String {
    // TODO use AndroidDomUtil.getResourceReferenceConverter()
    return if (this.startsWith("@string/")) {
        val key = this.removePrefix("@string/")
        GetAndroidStrings.getValue(facet, key) ?: ""
    } else {
        this
    }
}

data class DeepLink(val packageName: String, val scheme: String, val host: String, val path: String?) {
    val link = "${scheme}://${host}${path ?: ""}"
    override fun toString(): String {
        return link
    }
}