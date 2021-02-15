package com.github.mwsmith3.adbtools.deeplinks

import org.jetbrains.android.facet.AndroidFacet

class DeepLinkProviderServiceFake : DeepLinkProviderService {

    var someDeepLinks: List<String>? = null

    override fun getDeepLinks(facet: AndroidFacet): List<String> {
        return someDeepLinks ?: throw RuntimeException("deep links not set")
    }
}
