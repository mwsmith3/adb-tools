package com.github.mwsmith3.adbtools.window

import com.android.tools.idea.run.ConnectedAndroidDevice
import com.intellij.openapi.project.Project
import org.jetbrains.android.facet.AndroidFacet

class AdbToolsModel(private val project: Project) {

    private val stateListeners = mutableListOf<StateListener>()
    private val devices = mutableListOf<ConnectedAndroidDevice>()
    private val facets = mutableListOf<AndroidFacet>()
    private val deepLinks = mutableListOf<String>()

    interface StateListener {
        fun devicesSet(devices: List<ConnectedAndroidDevice>)
        fun facetsSet(facets: List<AndroidFacet>)
        fun deepLinksSet(deepLinks: List<String>)
    }

    fun addStateListener(listener: StateListener) {
        stateListeners.add(listener)
    }

    fun removeStateListener(listener: StateListener) {
        stateListeners.remove(listener)
    }

    fun setDevices(devices: List<ConnectedAndroidDevice>) {
        this.devices.clear()
        this.devices.addAll(devices)
        stateListeners.forEach {
            it.devicesSet(devices)
        }
    }

    fun setFacets(facets: List<AndroidFacet>) {
        this.facets.clear()
        this.facets.addAll(facets)
        stateListeners.forEach {
            it.facetsSet(this.facets)
        }
    }

    fun setDeepLinks(deepLinks: List<String>) {
        this.deepLinks.clear()
        this.deepLinks.addAll(deepLinks)
        stateListeners.forEach {
            it.deepLinksSet(deepLinks)
        }
    }
}
