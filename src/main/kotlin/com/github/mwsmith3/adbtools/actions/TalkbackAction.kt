package com.github.mwsmith3.adbtools.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.ui.Messages

class TalkbackAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        ApplicationManager.getApplication().invokeLater {
            Messages.showInfoMessage("TalkbackAction", "You triggerd this action")
        }
    }
}