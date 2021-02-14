package com.github.mwsmith3.adbtools.util

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.jetbrains.android.facet.AndroidFacet

class AndroidFacetProviderServiceFake : AndroidFacetProviderService {

    private val facetsSubject = BehaviorSubject.create<List<AndroidFacet>>()

    fun pushNewFacets(facets: List<AndroidFacet>) = facetsSubject.onNext(facets)

    fun pushError(error: Throwable) = facetsSubject.onError(error)

    override fun observe(): Observable<List<AndroidFacet>> {
        return facetsSubject
    }
}
