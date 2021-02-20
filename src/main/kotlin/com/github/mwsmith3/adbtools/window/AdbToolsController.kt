package com.github.mwsmith3.adbtools.window

import com.android.tools.idea.run.ConnectedAndroidDevice
import com.github.mwsmith3.adbtools.device.DeviceProviderService
import com.github.mwsmith3.adbtools.util.AndroidFacetProviderService
import com.github.mwsmith3.adbtools.util.ExecutorProviderService
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import io.reactivex.rxjava3.schedulers.Schedulers

class AdbToolsController(project: Project, private val adbToolsModel: AdbToolsModelSubject) {

    private val deviceProviderService = project.getService(DeviceProviderService::class.java)
    private val executorProvider = ServiceManager.getService(ExecutorProviderService::class.java)
    private val androidFacetProviderService = project.getService(AndroidFacetProviderService::class.java)

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
                    adbToolsModel.emitAdbState(AdbState.Connected(devices))
                },
                {
                    adbToolsModel.emitAdbState(AdbState.Error)
                }
            )
    }

    private fun observeFacets() {
        androidFacetProviderService.observe()
            .subscribeOn(Schedulers.from(executorProvider.edt))
            .subscribe { facets ->
                adbToolsModel.emitFacets(facets)
            }
    }
}
