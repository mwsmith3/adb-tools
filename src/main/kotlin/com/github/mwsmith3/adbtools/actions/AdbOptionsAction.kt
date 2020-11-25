package com.github.mwsmith3.adbtools.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.ui.popup.JBPopupFactory
import java.awt.MouseInfo

abstract class AdbOptionsAction : AdbAction() {

    fun <T> showOptions(event: AnActionEvent, title: String, options: List<T>, onOptionChosen: (T) -> Unit) {
        val point = MouseInfo.getPointerInfo().location
        val component = event.dataContext.getData(PlatformDataKeys.CONTEXT_COMPONENT)

        if (component != null) {
            JBPopupFactory.getInstance().createPopupChooserBuilder(
                    options
            ).setItemChosenCallback {
                onOptionChosen(it)
            }
                    .setTitle(title)
                    .createPopup().showInScreenCoordinates(component, point)
        }
    }
}