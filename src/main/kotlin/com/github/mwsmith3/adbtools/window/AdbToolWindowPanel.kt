package com.github.mwsmith3.adbtools.window

import com.android.ddmlib.IDevice
import com.android.tools.idea.run.ConnectedAndroidDevice
import com.github.mwsmith3.adbtools.deeplinks.DeepLinkData
import com.github.mwsmith3.adbtools.deeplinks.DeepLinkParser
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.panel
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import icons.AndroidIcons
import org.jetbrains.android.facet.AndroidFacet
import javax.swing.SwingConstants

class AdbToolWindowPanel(private val project: Project, private val model: AdbToolsModel) : SimpleToolWindowPanel(true, false) {

    private val deepLinks = DeepLinkParser.getDeepLinks(project)

    private val openDeepLinkAction = ActionManager.getInstance().getAction("com.github.mwsmith3.adbtools.deeplink")

    private val deepLinkComboBoxModel = CollectionComboBoxModel(deepLinks)

    private lateinit var comboBox: ComboBox<ConnectedAndroidDevice>

    private val deviceListCellRenderer = SimpleListCellRenderer.create<ConnectedAndroidDevice>("")
    { it?.name?.replace(Regex("[]]|[\\[]|"), "")?.replace('_', ' ') }

    private val facetListCellRenderer = SimpleListCellRenderer.create<AndroidFacet>("") {
        it.module.name
    }

    private val windowContent = panel {
        row {
            comboBox = comboBox(model.deviceComboModel, {
                model.deviceComboModel.selected
            }, {
                model.deviceComboModel.selectedItem = it
            }, deviceListCellRenderer).constraints(CCFlags.growX).component
            comboBox(model.facetComboModel, {
                model.facetComboModel.selected
            }, {
                model.facetComboModel.selectedItem = it
            }, facetListCellRenderer).constraints(CCFlags.growX)
        }

//            buttonFromAction("DL", ActionPlaces.TOOLBAR, openDeepLinkAction)
//        }.growX
//        row {
//            label("Query parameter:")
//            textField({ queryParam }, { queryParam = it }, 1)
//        }
    }

    private val emptyContent = panel {
        row {
            val emptyLabel = label("Connect a device via USB cable or run an Android Virtual Device")
                    .constraints(CCFlags.pushX, CCFlags.pushY, CCFlags.growX, CCFlags.growY).component
            emptyLabel.icon = AndroidIcons.DeviceExplorer.DevicesLineup
            emptyLabel.horizontalAlignment = SwingConstants.CENTER
            emptyLabel.horizontalTextPosition = SwingConstants.CENTER
            emptyLabel.verticalTextPosition = SwingConstants.BOTTOM
            emptyLabel.font = JBUI.Fonts.label(13f)
            emptyLabel.foreground = UIUtil.getInactiveTextColor()
        }
    }

    init {
        this.toolbar = createActionToolbar("com.github.mwsmith3.adbtools.window.actions").component
        this.setContent(emptyContent)
        model.addStateListener(object : AdbToolsModel.StateListener {
            override fun onDeviceConnected() {
                this@AdbToolWindowPanel.setContent(windowContent)
            }
            override fun onNoDevicesConnected() {
                this@AdbToolWindowPanel.setContent(emptyContent)
            }
        })
    }

    private fun getSelectedDeepLink() = deepLinkComboBoxModel.selected

    private fun createActionToolbar(actionId: String): ActionToolbar {
        val actionManager = ActionManager.getInstance()
        val actionGroup = actionManager.getAction(actionId) as DefaultActionGroup
        return actionManager.createActionToolbar(ActionPlaces.TOOLWINDOW_CONTENT, actionGroup, true).apply {
            this.setTargetComponent(this@AdbToolWindowPanel)
        }
    }

    override fun getData(dataId: String): Any? {
        return when {
            DEVICE_KEY.`is`(dataId) -> {
                model.getSelectedDevice()
            }
            FACET_KEY.`is`(dataId) -> {
                model.facetComboModel.selected as AndroidFacet
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

    companion object {
        val DEVICE_KEY = DataKey.create<IDevice>("device")
        val DEEP_LINK_KEY = DataKey.create<DeepLinkData>("deep link")
        val FACET_KEY = DataKey.create<AndroidFacet>("android facet")
    }
}