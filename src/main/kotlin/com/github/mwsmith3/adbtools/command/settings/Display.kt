package com.github.mwsmith3.adbtools.command.settings

sealed class Display {
    data class Spec(val x: Int, val y: Int, val density: Int, val deviceName: String) : Display() {
        override fun toString() = "${x}x$y px, (${density}ppi), ($deviceName)"
    }
    object Reset : Display() {
        override fun toString() = "Reset"
    }

    companion object {
        val LIST = listOf(
            Reset,
            Spec(480, 800, 254, "Nexus One"),
            Spec(720, 1280, 316, "Galaxy Nexus"),
            Spec(1080, 1920, 441, "Pixel"),
            Spec(1440, 2560, 534, "Pixel XL"),
            Spec(1080, 2280, 444, "Pixel 4"),
            Spec(1440, 3040, 537, "Pixel 4 XL"),
            Spec(1080, 2340, 432, "Pixel 5"),
            Spec(800, 1280, 216, "Nexus 7 2012"),
            Spec(1600, 2560, 300, "Nexus 10"),
            Spec(1800, 2560, 308, "Pixel C"),
            Spec(2000, 3000, 293, "Pixel Slate"),
        )
    }
}
