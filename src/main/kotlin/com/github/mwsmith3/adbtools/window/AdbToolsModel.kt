package com.github.mwsmith3.adbtools.window

import com.android.tools.idea.run.ConnectedAndroidDevice
import org.jetbrains.android.facet.AndroidFacet

data class AdbToolsModel(val adbState: AdbState = AdbState.Loading, val facets: List<AndroidFacet> = emptyList())

sealed class AdbState {
    object Loading : AdbState()
    object Error : AdbState()
    data class Connected(val devices: List<ConnectedAndroidDevice>) : AdbState()
}
