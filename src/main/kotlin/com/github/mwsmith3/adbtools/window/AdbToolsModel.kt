package com.github.mwsmith3.adbtools.window

import com.android.tools.idea.run.ConnectedAndroidDevice
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.jetbrains.android.facet.AndroidFacet

class AdbToolsModel : ObservableAdbToolsModel, AdbToolsModelSubject {

    private val stateSubject = BehaviorSubject.create<AdbState>()
    private val facetSubject = BehaviorSubject.create<List<AndroidFacet>>()

    init {
        stateSubject.onNext(AdbState.Loading)
    }

    override fun observeAdbState(): Observable<AdbState> = stateSubject

    override fun observeFacets(): Observable<List<AndroidFacet>> = facetSubject

    override fun emitAdbState(state: AdbState) {
        stateSubject.onNext(state)
    }

    override fun emitFacets(facets: List<AndroidFacet>) {
        facetSubject.onNext(facets)
    }
}

sealed class AdbState {
    object Loading : AdbState()
    object Error : AdbState()
    data class Connected(val devices: List<ConnectedAndroidDevice>) : AdbState()
}

interface ObservableAdbToolsModel {
    fun observeAdbState(): Observable<AdbState>
    fun observeFacets(): Observable<List<AndroidFacet>>
}

interface AdbToolsModelSubject {
    fun emitAdbState(state: AdbState)
    fun emitFacets(facets: List<AndroidFacet>)
}
