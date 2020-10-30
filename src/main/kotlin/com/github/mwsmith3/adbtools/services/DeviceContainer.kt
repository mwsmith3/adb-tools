package com.github.mwsmith3.adbtools.services

import com.android.ddmlib.IDevice

class DeviceContainer {

    private val deviceList = mutableListOf<IDevice>()

    fun add(device: IDevice) {
        deviceList.add(device)
    }

    fun remove(device: IDevice): Int {
        val index = deviceList.indexOf(device)
        deviceList.remove(device)
        return index
    }

    fun isEmpty(): Boolean {
        return deviceList.isEmpty()
    }
}