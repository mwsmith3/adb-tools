package com.github.mwsmith3.adbtools.command.settings

import java.text.DecimalFormat

sealed class AnimationScale {
    abstract val value: Float

    data class On(override val value: Float) : AnimationScale() {
        override fun toString() = "${formatter.format(value)}x"
    }

    object Off : AnimationScale() {
        override val value = 0F
        override fun toString() = "Off"
    }

    companion object {
        private val formatter = DecimalFormat("#.#").apply {
            minimumIntegerDigits = 0
        }

        val LIST = listOf(
            Off,
            On(0.5F),
            On(1F),
            On(1.5F),
            On(2F),
            On(5F),
            On(10F),
        )
    }
}
