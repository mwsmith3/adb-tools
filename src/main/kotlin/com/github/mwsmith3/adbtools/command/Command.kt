package com.github.mwsmith3.adbtools.command

import com.android.ddmlib.IDevice
import com.android.ddmlib.MultiLineReceiver
import java.util.concurrent.TimeUnit

abstract class Command<T> {
    abstract val adbCommand: String
    abstract val description: String
    protected val output = mutableListOf<String>()

    fun run(device: IDevice): T {
        device.executeShellCommand(adbCommand, Receiver(), TIMEOUT, TimeUnit.SECONDS)
        return resolve()
    }

    protected abstract fun resolve(): T

    companion object {
        const val TIMEOUT = 15L
    }

    private inner class Receiver : MultiLineReceiver() {
        override fun processNewLines(lines: Array<out String>) {
            output.addAll(lines.toList())
        }
        override fun isCancelled() = false
    }
}
