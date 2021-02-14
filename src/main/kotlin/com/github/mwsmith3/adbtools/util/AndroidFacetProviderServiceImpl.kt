package com.github.mwsmith3.adbtools.util

import com.intellij.openapi.project.Project
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.jetbrains.android.facet.AndroidFacet
import org.jetbrains.android.util.AndroidUtils

class AndroidFacetProviderServiceImpl(project: Project) : AndroidFacetProviderService {

    private val androidFacetSubject = BehaviorSubject.create<List<AndroidFacet>>()

    init {
        val facets = AndroidUtils.getApplicationFacets(project)
        androidFacetSubject.onNext(facets)
    }

    override fun observe(): Observable<List<AndroidFacet>> {
        return androidFacetSubject
    }
}
