package com.github.mwsmith3.adbtools.window

import com.android.tools.idea.run.ConnectedAndroidDevice
import com.github.mwsmith3.adbtools.device.DeviceProviderService
import com.github.mwsmith3.adbtools.util.AndroidFacetProviderService
import com.github.mwsmith3.adbtools.util.ExecutorProviderService
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject

class AdbToolsController(project: Project) {

    private val deviceProviderService = project.getService(DeviceProviderService::class.java)
    private val executorProvider = ServiceManager.getService(ExecutorProviderService::class.java)
    private val androidFacetProviderService = project.getService(AndroidFacetProviderService::class.java)

    private val _model = BehaviorSubject.createDefault(AdbToolsModel(AdbState.Loading, emptyList()))
    val model: Observable<AdbToolsModel>
        get() = _model

    init {
        observeFacets()
        observeDevices()
    }

    private fun observeDevices() {
        deviceProviderService.observe()
            .subscribeOn(Schedulers.from(executorProvider.tasks))
            .map { iDeviceList ->
                iDeviceList.map { iDevice ->
                    ConnectedAndroidDevice(iDevice, null)
                }
            }
            .observeOn(Schedulers.from(executorProvider.edt))
            .subscribe(
                { devices ->
                    updateAndEmit {
                        it.copy(adbState = AdbState.Connected(devices))
                    }
                },
                {
                    updateAndEmit {
                        it.copy(adbState = AdbState.Error)
                    }
                }
            )
    }

    private fun observeFacets() {
        androidFacetProviderService.observe()
            .subscribeOn(Schedulers.from(executorProvider.edt))
            .subscribe(
                { facets ->
                    updateAndEmit {
                        it.copy(facets = facets)
                    }
                },
                {
                    updateAndEmit {
                        it.copy(facets = emptyList())
                    }
                }
            )
    }

    private fun updateAndEmit(transform: (AdbToolsModel) -> AdbToolsModel) {
        val newState = transform(_model.value)
        _model.onNext(newState)
    }
}
