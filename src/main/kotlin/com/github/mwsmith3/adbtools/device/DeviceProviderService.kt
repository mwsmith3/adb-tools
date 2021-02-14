package com.github.mwsmith3.adbtools.device

import com.android.ddmlib.IDevice
import com.intellij.openapi.Disposable
import io.reactivex.rxjava3.core.Observable

interface DeviceProviderService : Disposable {
    fun observe(): Observable<List<IDevice>>
}
