package com.github.mwsmith3.adbtools.deeplinks

import org.jetbrains.android.facet.AndroidFacet

interface DeepLinkProviderService {
    fun getDeepLinks(facet: AndroidFacet): List<String>
}
