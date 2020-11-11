package com.github.mwsmith3.adbtools.listeners

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.actions.adbexecutable.AdbExecutable
import com.intellij.openapi.project.Project
import com.intellij.util.messages.Topic

interface AdbExecutor {
    fun execute(device: IDevice, adbExecutable: AdbExecutable)

    companion object {
        val ADB_EXECUTOR_TOPIC = Topic.create("adb executor topic", AdbExecutor::class.java)
    }
}

fun Project?.publishAdbExecutable(device: IDevice, adbExecutable: AdbExecutable) {
    this?.messageBus?.syncPublisher(AdbExecutor.ADB_EXECUTOR_TOPIC)?.execute(device, adbExecutable)
}