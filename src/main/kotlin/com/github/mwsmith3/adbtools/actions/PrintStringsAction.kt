package com.github.mwsmith3.adbtools.actions

import com.github.mwsmith3.adbtools.util.NotificationHelper
import com.github.mwsmith3.adbtools.window.AdbToolWindowPanel.Companion.STRINGS_KEY
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class PrintStringsAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val strings = e.getData(STRINGS_KEY)
        if (strings != null) {
            NotificationHelper.info("Android strings: $strings")
        }
    }
}