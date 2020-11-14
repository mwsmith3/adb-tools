package com.github.mwsmith3.adbtools.window

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.util.DeepLink
import com.github.mwsmith3.adbtools.util.DeepLinkParser
import com.github.mwsmith3.adbtools.util.GetAndroidStrings
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.DataKey
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.layout.PropertyBinding
import com.intellij.ui.layout.panel
import org.jetbrains.android.facet.AndroidFacet
import java.util.*
import javax.swing.DefaultComboBoxModel

class AdbToolWindowPanel(private val device: IDevice, private val project: Project) : SimpleToolWindowPanel(true, false) {

    // TODO add header
    // TODO fix icon

    private val deepLinks: Vector<DeepLink> = Vector(DeepLinkParser.getDeepLinks(project))
    private var selected = 0

    private val openDeepLinkAction = ActionManager.getInstance().getAction("com.github.mwsmith3.adbtools.deeplink")

    private val windowContent = panel {
        row {
            label("Deep links:")
            comboBox(DefaultComboBoxModel(deepLinks), {
                getSelectedDeepLink()
            }, {
                selected = deepLinks.indexOf(it)
            })
            buttonFromAction("DL", ActionPlaces.TOOLBAR, openDeepLinkAction)
        }
//        row {
//            label("Query parameter:")
//            textField({ queryParam }, { queryParam = it }, 1)
//        }
    }

    init {

        val actionManager = ActionManager.getInstance()
        val actionGroup = actionManager.getAction("com.github.mwsmith3.adbtools.window.actions") as DefaultActionGroup
        val actionToolbar = actionManager.createActionToolbar(ActionPlaces.TOOLBAR, actionGroup, true)
        this.toolbar = actionToolbar.component
        actionToolbar.setTargetComponent(this)
        this.setContent(windowContent)
    }

    private fun getSelectedDeepLink(): DeepLink? {
        return deepLinks.getOrNull(selected)
    }

    override fun getData(dataId: String): Any? {
        return when {
            DEVICE_KEY.`is`(dataId) -> {
                device
            }
            STRINGS_KEY.`is`(dataId) -> {
                getStrings()
            }
            DEEP_LINK_KEY.`is`(dataId) -> {
                getSelectedDeepLink()?.let {
                    DeepLinkData(it, false, "", "")
                }
            }
            else -> {
                super.getData(dataId)
            }
        }
    }

    private fun getStrings(): List<AndroidString> {
        return GetAndroidStrings.get(project)
    }

    companion object {
        val DEVICE_KEY = DataKey.create<IDevice>("device")
        val DEEP_LINK_KEY = DataKey.create<DeepLinkData>("deep link")
        val STRINGS_KEY = DataKey.create<List<AndroidString>>("android strings")
    }
}

data class DeepLinkData(val deepLink: DeepLink, val attachDebugger: Boolean, val param: String, val value: String)
data class AndroidString(val key: String, val value: String)