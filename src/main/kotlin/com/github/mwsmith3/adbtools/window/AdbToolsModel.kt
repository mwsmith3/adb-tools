package com.github.mwsmith3.adbtools.window

import com.android.tools.idea.run.ConnectedAndroidDevice
import com.github.mwsmith3.adbtools.deeplinks.DeepLink
import com.intellij.openapi.project.Project
import org.jetbrains.android.facet.AndroidFacet
import org.jetbrains.android.util.AndroidUtils

class AdbToolsModel(private val project: Project) {

    private val stateListeners = mutableListOf<StateListener>()
    private val devices = mutableListOf<ConnectedAndroidDevice>()
    private val facets = mutableListOf<AndroidFacet>()
    private val deepLinks = mutableListOf<String>()

    interface StateListener {
        fun deviceAdded(device: ConnectedAndroidDevice)
        fun deviceRemoved(device: ConnectedAndroidDevice)
        fun deviceUpdated(device: ConnectedAndroidDevice)
        fun devicesCleared()
        fun facetsSet(facets: List<AndroidFacet>)
        fun deepLinksSet(deepLinks: List<String>)
    }

    fun addStateListener(listener: StateListener) {
        stateListeners.add(listener)
    }

    fun removeStateListener(listener: StateListener) {
        stateListeners.remove(listener)
    }

    fun addDevice(device: ConnectedAndroidDevice) {
        devices.add(device)
        stateListeners.forEach {
            it.deviceAdded(device)
        }
    }

    fun removeDevice(device: ConnectedAndroidDevice) {
        devices.remove(device)
        stateListeners.forEach {
            it.deviceRemoved(device)
        }
    }

    fun updateDevice(device: ConnectedAndroidDevice) {
        stateListeners.forEach {
            it.deviceUpdated(device)
        }
    }

    fun clearDevices() {
        devices.clear()
        stateListeners.forEach {
            it.devicesCleared()
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