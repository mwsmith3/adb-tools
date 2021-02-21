package com.github.mwsmith3.adbtools.device

import com.android.ddmlib.IDevice
import io.reactivex.rxjava3.core.Observable

class DeviceProviderServiceFake : DeviceProviderService {

    var observable: Observable<List<IDevice>> = Observable.never()

    override fun observe(): Observable<List<IDevice>> = observable

    override fun dispose() { }
}
