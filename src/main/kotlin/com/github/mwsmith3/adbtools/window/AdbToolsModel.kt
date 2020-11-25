package com.github.mwsmith3.adbtools.window

import com.android.ddmlib.IDevice
import com.android.tools.idea.run.ConnectedAndroidDevice
import com.github.mwsmith3.adbtools.device.DeviceProviderService
import com.intellij.openapi.project.Project
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.MutableCollectionComboBoxModel
import org.jetbrains.android.facet.AndroidFacet
import org.jetbrains.android.util.AndroidUtils

class AdbToolsModel(private val project: Project) : DeviceProviderService.DeviceChangeListener {

    private val _deviceComboModel = MutableCollectionComboBoxModel<ConnectedAndroidDevice>()
    val deviceComboModel: CollectionComboBoxModel<ConnectedAndroidDevice> = _deviceComboModel

    private val _facetComboModel = MutableCollectionComboBoxModel<AndroidFacet>()
    val facetComboModel: CollectionComboBoxModel<AndroidFacet> = _facetComboModel

    private val stateListeners = mutableListOf<StateListener>()

    interface StateListener {
        fun onDeviceConnected()
        fun onNoDevicesConnected()
    }

    init {
        val facets = AndroidUtils.getApplicationFacets(project)
        if (facets.isNotEmpty()) {
            facetComboModel.add(facets)
            facetComboModel.selectedItem = facets[0]
        }
    }

    fun addStateListener(listener: StateListener) {
        stateListeners.add(listener)
    }

    fun removeStateListener(listener: StateListener) {
        stateListeners.remove(listener)
    }

    fun getSelectedDevice(): IDevice? {
        return _deviceComboModel.selected?.device
    }

    override fun deviceAdded(device: ConnectedAndroidDevice) {
        _deviceComboModel.addItem(device)
        _deviceComboModel.selectedItem = device
        if (_deviceComboModel.size == 1) {
            stateListeners.forEach {
                it.onDeviceConnected()
            }
        }
    }

    override fun deviceRemoved(device: ConnectedAndroidDevice) {
        _deviceComboModel.remove(device)
        if (_deviceComboModel.size == 0) {
            stateListeners.forEach {
                it.onNoDevicesConnected()
            }
        }
    }

    override fun deviceChanged(device: ConnectedAndroidDevice) {
        _deviceComboModel.contentsChanged(device)
    }
}