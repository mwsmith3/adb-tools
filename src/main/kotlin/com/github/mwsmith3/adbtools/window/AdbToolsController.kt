package com.github.mwsmith3.adbtools.window

import com.android.tools.idea.run.ConnectedAndroidDevice
import com.github.mwsmith3.adbtools.deeplinks.DeepLinkParser
import com.github.mwsmith3.adbtools.device.DeviceProviderService
import com.github.mwsmith3.adbtools.util.ExecutorProviderService
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import io.reactivex.rxjava3.schedulers.Schedulers
import org.jetbrains.android.facet.AndroidFacet
import org.jetbrains.android.util.AndroidUtils

class AdbToolsController(val project: Project, private val model: AdbToolsModel, private val view: AdbToolsWindowView) {

    private val deviceProviderService = project.getService(DeviceProviderService::class.java)
    private val executorProvider = ServiceManager.getService(ExecutorProviderService::class.java)

    init {
        observeDevices()
        observeFacetSelection()
        model.setFacets(getFacets())
    }

    private fun observeFacetSelection() {
        view.facetSelectionObservable
            .subscribeOn(Schedulers.from(executorProvider.tasks))
            .map {
                getDeepLinks(it)
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

    private fun getFacets() = AndroidUtils.getApplicationFacets(project)

    private fun getDeepLinks(facet: AndroidFacet?): List<String> {
        val dl = facet?.let { DeepLinkParser.getDeepLinks(it) } ?: emptyList()
        Logger.getInstance(AdbToolsController::class.java).info("$dl")
        return dl.map { it.link }
    }
}
