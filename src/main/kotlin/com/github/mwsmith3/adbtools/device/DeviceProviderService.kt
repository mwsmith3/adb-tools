package com.github.mwsmith3.adbtools.device

import com.android.ddmlib.AndroidDebugBridge
import com.android.tools.idea.run.ConnectedAndroidDevice
import com.google.common.util.concurrent.ListenableFuture
import com.intellij.openapi.Disposable
import io.reactivex.rxjava3.core.Observable

interface DeviceProviderService : Disposable {
    fun setup(listenableFutureBridge: ListenableFuture<AndroidDebugBridge>)
    fun observe(): Observable<State>

    sealed class State {
        object Loading : State()
        data class Error(val throwable: Throwable) : State()
        data class Success(val devices: List<ConnectedAndroidDevice>) : State()
    }
}
