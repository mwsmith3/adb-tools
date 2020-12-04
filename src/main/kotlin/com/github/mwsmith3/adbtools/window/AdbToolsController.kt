package com.github.mwsmith3.adbtools.window

import com.android.ddmlib.AndroidDebugBridge
import com.android.tools.idea.run.ConnectedAndroidDevice
import com.github.mwsmith3.adbtools.deeplinks.DeepLinkParser
import com.github.mwsmith3.adbtools.device.DeviceProviderService
import com.github.mwsmith3.adbtools.device.DeviceProviderServiceListener
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.util.concurrency.EdtExecutorService
import org.jetbrains.android.facet.AndroidFacet
import org.jetbrains.android.util.AndroidUtils

class AdbToolsController(private val project: Project,
                         private val model: AdbToolsModel,
                         private val view: AdbToolsWindowView,
                         private val deviceProviderService: DeviceProviderService) {

    private val edtExecutor = EdtExecutorService.getInstance()

    fun setup() {
        view.addListener(ViewListener())
        model.setFacets(getFacets())
        deviceProviderService.addDeviceProviderServiceListener(ServiceListener())
        Futures.addCallback(deviceProviderService.start(), AdbStartHandler(), edtExecutor)
    }

    private fun getFacets() = AndroidUtils.getApplicationFacets(project)

    private fun refreshDevices() {
        // TODO notify view to show loading
        Logger.getInstance(AdbToolsController::class.java).info("Refreshing devices")
        model.clearDevices()
        val devices = deviceProviderService.getDevices()
        devices.forEach {
            model.addDevice(it)
        }
    }

    private fun getDeepLinks(facet: AndroidFacet?): List<String> {
        val dl = facet?.let { DeepLinkParser.getDeepLinks(it) } ?: emptyList()
        Logger.getInstance(AdbToolsController::class.java).info("$dl")
        return dl.map { it.link }
    }

    private inner class ServiceListener : DeviceProviderServiceListener {
        override fun deviceAdded(device: ConnectedAndroidDevice) {
            model.addDevice(device)
        }

        override fun deviceRemoved(device: ConnectedAndroidDevice) {
            model.removeDevice(device)
        }

        override fun deviceChanged(device: ConnectedAndroidDevice) {
            model.updateDevice(device)
        }

        override fun serviceRestarted() {
            refreshDevices()
        }
    }

    private inner class AdbStartHandler : FutureCallback<AndroidDebugBridge> {
        override fun onSuccess(bridge: AndroidDebugBridge?) {
            Logger.getInstance(AdbToolsController::class.java).info("Got bridge: $bridge")
            refreshDevices()
        }

        override fun onFailure(error: Throwable) {
            Logger.getInstance(AdbToolsController::class.java).info("Unable to get bridge: ${error.message}")
        }
    }

    private inner class ViewListener : AdbToolsWindowViewListener {
        override fun onFacetSelected(facet: AndroidFacet?) {
            val deepLinks = getDeepLinks(facet)
            model.setDeepLinks(deepLinks)
        }
    }
}