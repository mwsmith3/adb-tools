package com.github.mwsmith3.adbtools.window

import com.intellij.ui.layout.panel

object AdbToolWindow {

    val emptyContent = panel {
        row {
            cell(isFullWidth = true) {

                label("No devices connected")
            }
        }
    }
}