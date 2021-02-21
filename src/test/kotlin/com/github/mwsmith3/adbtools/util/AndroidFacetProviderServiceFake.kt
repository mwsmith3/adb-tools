package com.github.mwsmith3.adbtools.util

import io.reactivex.rxjava3.core.Observable
import org.jetbrains.android.facet.AndroidFacet

class AndroidFacetProviderServiceFake : AndroidFacetProviderService {

    var observable: Observable<List<AndroidFacet>> = Observable.never()

    override fun observe(): Observable<List<AndroidFacet>> = observable
}
