package com.github.mwsmith3.adbtools.command.settings

import com.github.mwsmith3.adbtools.command.NoResultCommand

class SetAnimationScalesCommand(private val scale: AnimationScale) : NoResultCommand() {
    override val adbCommand: String
        get() = "settings put global window_animation_scale ${scale.value} && " +
            "settings put global transition_animation_scale ${scale.value} && " +
            "settings put global animator_duration_scale ${scale.value}"

    override val description: String
        get() = "Set animation scale"
}
