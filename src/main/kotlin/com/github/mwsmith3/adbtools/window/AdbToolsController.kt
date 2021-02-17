package com.github.mwsmith3.adbtools.window

import com.android.tools.idea.run.ConnectedAndroidDevice
import com.github.mwsmith3.adbtools.deeplinks.DeepLinkProviderService
import com.github.mwsmith3.adbtools.device.DeviceProviderService
import com.github.mwsmith3.adbtools.util.AndroidFacetProviderService
import com.github.mwsmith3.adbtools.util.ExecutorProviderService
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject

class AdbToolsController(
    project: Project,
    private val view: AdbToolsWindowView,
    private val modelSubject: BehaviorSubject<AdbToolsModel>
) {
    private val deviceProviderService = project.getService(DeviceProviderService::class.java)
    private val executorProvider = ServiceManager.getService(ExecutorProviderService::class.java)
    private val androidFacetProviderService = project.getService(AndroidFacetProviderService::class.java)
    private val deepLinkProviderService = project.getService(DeepLinkProviderService::class.java)

    private var model = AdbToolsModel()

    init {
        modelSubject.onNext(model)
        observeFacets()
        observeDevices()
        observeFacetSelection()
    }

    private fun observeFacetSelection() {
        view.facetSelectionObservable
            .distinctUntilChanged()
            .subscribeOn(Schedulers.from(executorProvider.tasks))
            .map {
                deepLinkProviderService.getDeepLinks(it)
            }
            .observeOn(Schedulers.from(executorProvider.edt))
            .subscribe { deepLinks ->
                model = model.copy(deepLinks = deepLinks)
                modelSubject.onNext(model)
            }
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
                    model = model.copy(adbState = AdbState.Connected(devices))
                    modelSubject.onNext(model)
                },
                {
                    model = model.copy(adbState = AdbState.Error)
                    modelSubject.onNext(model)
                }
            )
    }

    private fun observeFacets() {
        androidFacetProviderService.observe()
            .subscribeOn(Schedulers.from(executorProvider.edt))
            .subscribe { facets ->
                model = model.copy(facets = facets)
                modelSubject.onNext(model)
            }
    }
}
