package com.github.mwsmith3.adbtools.adb

import com.android.ddmlib.MultiLineReceiver

class GenericReceiver : MultiLineReceiver() {

    private val _shellOutput = mutableListOf<String>()
    val shellOutput: List<String> = _shellOutput

    override fun processNewLines(lines: Array<String>) {
        _shellOutput.addAll(listOf(*lines))
    }

    override fun isCancelled() = false
}