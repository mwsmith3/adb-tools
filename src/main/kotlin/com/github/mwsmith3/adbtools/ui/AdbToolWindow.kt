package com.github.mwsmith3.adbtools.ui

import com.intellij.ui.layout.panel

object AdbToolWindow {
    val emptyContent = panel {
        row {
            label("No devices connected")
        }
    }
}