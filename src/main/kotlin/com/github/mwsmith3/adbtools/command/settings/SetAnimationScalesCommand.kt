package com.github.mwsmith3.adbtools.command.settings

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.adb.GenericReceiver
import com.github.mwsmith3.adbtools.command.Command
import java.util.concurrent.TimeUnit

class SetAnimationScalesCommand(private val scale: AnimationScale) : Command<Unit> {
    override val command: String
        get() = "settings put global window_animation_scale ${scale.value} && " +
                "settings put global transition_animation_scale ${scale.value} && " +
                "settings put global animator_duration_scale ${scale.value}"

    override val description: String
        get() = "Set animation scale"

    override fun run(device: IDevice) {
        device.executeShellCommand(command, GenericReceiver(), 15L, TimeUnit.SECONDS)
    }
}