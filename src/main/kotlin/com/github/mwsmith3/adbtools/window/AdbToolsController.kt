package com.github.mwsmith3.adbtools.window

import com.github.mwsmith3.adbtools.deeplinks.DeepLinkParser
import com.github.mwsmith3.adbtools.device.DeviceProviderService
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.util.concurrency.EdtExecutorService
import io.reactivex.rxjava3.schedulers.Schedulers
import org.jetbrains.android.facet.AndroidFacet
import org.jetbrains.android.util.AndroidUtils

class AdbToolsController(val project: Project, val model: AdbToolsModel, private val view: AdbToolsWindowView) {

    private val deviceProviderService = project.getService(DeviceProviderService::class.java)

    fun setup() {
        view.addListener(ViewListener())
        model.setFacets(getFacets())
        deviceProviderService.observe()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.from(EdtExecutorService.getInstance()))
            .subscribe {
                when (it) {
                    is DeviceProviderService.State.Success -> {
                        model.setDevices(it.devices)
                    }
                }
            }
    }

    private fun getFacets() = AndroidUtils.getApplicationFacets(project)

    private fun getDeepLinks(facet: AndroidFacet?): List<String> {
        val dl = facet?.let { DeepLinkParser.getDeepLinks(it) } ?: emptyList()
        Logger.getInstance(AdbToolsController::class.java).info("$dl")
        return dl.map { it.link }
    }

    private inner class ViewListener : AdbToolsWindowViewListener {
        override fun onFacetSelected(facet: AndroidFacet?) {
            val deepLinks = getDeepLinks(facet)
            model.setDeepLinks(deepLinks)
        }
    }
}
