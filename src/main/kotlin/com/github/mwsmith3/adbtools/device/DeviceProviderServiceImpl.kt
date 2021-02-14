package com.github.mwsmith3.adbtools.device

import com.android.ddmlib.AndroidDebugBridge
import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.util.ExecutorProviderService
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class DeviceProviderServiceImpl(project: Project) : DeviceProviderService {

    private val executorProvider = ServiceManager.getService(ExecutorProviderService::class.java)
    private val bridgeProvider = ServiceManager.getService(BridgeProviderService::class.java)

    private var bridge: AndroidDebugBridge? = null

    private val iDeviceSubject = BehaviorSubject.create<List<IDevice>>()

    private val deviceChangeListener = object : AndroidDebugBridge.IDeviceChangeListener {
        override fun deviceConnected(device: IDevice?) {
            getDevicesAndEmit()
        }

        override fun deviceDisconnected(device: IDevice?) {
            getDevicesAndEmit()
        }

        override fun deviceChanged(device: IDevice?, changeMask: Int) {
            getDevicesAndEmit()
        }
    }

    private val adbChangeListener = AndroidDebugBridge.IDebugBridgeChangeListener { bridge ->
        this@DeviceProviderServiceImpl.bridge = bridge
        getDevicesAndEmit()
    }

    init {
        AndroidDebugBridge.addDeviceChangeListener(deviceChangeListener)
        AndroidDebugBridge.addDebugBridgeChangeListener(adbChangeListener)

        val listenableFutureBridgeProvider = bridgeProvider.getListenableFutureBridgeProvider()

        Futures.addCallback(
            listenableFutureBridgeProvider(project),
            object : FutureCallback<AndroidDebugBridge> {
                override fun onSuccess(bridge: AndroidDebugBridge?) {
                    this@DeviceProviderServiceImpl.bridge = bridge
                    getDevicesAndEmit()
                }
                override fun onFailure(error: Throwable) {
                    iDeviceSubject.onError(error)
                }
            },
            executorProvider.tasks
        )
    }

    override fun observe(): Observable<List<IDevice>> = iDeviceSubject

    private fun getDevicesAndEmit() {
        iDeviceSubject.onNext(
            getDevicesFromBridge()
        )
    }

    private fun getDevicesFromBridge(): List<IDevice> {
        return bridge?.devices?.toList() ?: emptyList()
    }

    override fun dispose() {
        AndroidDebugBridge.removeDeviceChangeListener(deviceChangeListener)
        AndroidDebugBridge.removeDebugBridgeChangeListener(adbChangeListener)
        bridge = null
    }
}
