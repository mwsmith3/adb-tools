package com.github.mwsmith3.adbtools.util

import io.reactivex.rxjava3.core.Observable
import org.jetbrains.android.facet.AndroidFacet

interface AndroidFacetProviderService {
    fun observe(): Observable<List<AndroidFacet>>
}
