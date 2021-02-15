package com.github.mwsmith3.adbtools.window

import com.android.tools.idea.run.ConnectedAndroidDevice
import com.github.mwsmith3.adbtools.deeplinks.DeepLinkProviderService
import com.github.mwsmith3.adbtools.device.DeviceProviderService
import com.github.mwsmith3.adbtools.util.AndroidFacetProviderService
import com.github.mwsmith3.adbtools.util.ExecutorProviderService
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import io.reactivex.rxjava3.schedulers.Schedulers

class AdbToolsController(val project: Project, private val model: AdbToolsModel, private val view: AdbToolsWindowView) {

    private val deviceProviderService = project.getService(DeviceProviderService::class.java)
    private val executorProvider = ServiceManager.getService(ExecutorProviderService::class.java)
    private val androidFacetProviderService = project.getService(AndroidFacetProviderService::class.java)
    private val deepLinkProviderService = project.getService(DeepLinkProviderService::class.java)

    init {
        observeFacets()
        observeDevices()
        observeFacetSelection()
    }

    private fun observeFacetSelection() {
        view.facetSelectionObservable
            .subscribeOn(Schedulers.from(executorProvider.tasks))
            .map {
                deepLinkProviderService.getDeepLinks(it)
            }
            .observeOn(Schedulers.from(executorProvider.edt))
            .subscribe {
                model.setDeepLinks(it)
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
                {
                    model.setDevices(it)
                },
                {
                    model.setDevices(emptyList())
                }
            )
    }

    private fun observeFacets() {
        androidFacetProviderService.observe()
            .subscribeOn(Schedulers.from(executorProvider.edt))
            .subscribe {
                model.setFacets(it)
            }
    }
}
