package com.github.mwsmith3.adbtools.window

import com.android.ddmlib.AndroidDebugBridge
import com.android.tools.idea.adb.AdbService
import com.github.mwsmith3.adbtools.deeplinks.DeepLinkParser
import com.github.mwsmith3.adbtools.device.DeviceProviderService
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.ThreadFactoryBuilder
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.util.concurrency.EdtExecutorService
import io.reactivex.rxjava3.schedulers.Schedulers
import org.jetbrains.android.facet.AndroidFacet
import org.jetbrains.android.sdk.AndroidSdkUtils
import org.jetbrains.android.util.AndroidUtils
import java.io.FileNotFoundException
import java.util.concurrent.Executors

class AdbToolsController(val project: Project, val model: AdbToolsModel, private val view: AdbToolsWindowView) {

    private val deviceProviderService = project.getService(DeviceProviderService::class.java)
    private val executor = Executors.newCachedThreadPool(ThreadFactoryBuilder().build())

    fun setup() {
        view.addListener(ViewListener())
        model.setFacets(getFacets())
        deviceProviderService.setup(
            get()
        )
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

    fun get(): ListenableFuture<AndroidDebugBridge> {
        val file = AndroidSdkUtils.getAdb(project)
        return if (file == null) {
            Futures.immediateFailedFuture(FileNotFoundException("Android debug bridge not found"))
        } else {
            AdbService.getInstance().getDebugBridge(file)
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
