package com.github.mwsmith3.adbtools.window

import org.jetbrains.android.facet.AndroidFacet

interface AdbToolsWindowViewListener {
    fun onFacetSelected(facet: AndroidFacet?)
}