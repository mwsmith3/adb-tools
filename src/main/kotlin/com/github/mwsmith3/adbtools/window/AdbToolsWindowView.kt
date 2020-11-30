package com.github.mwsmith3.adbtools.window

import com.android.ddmlib.IDevice
import com.android.tools.idea.run.ConnectedAndroidDevice
import com.github.mwsmith3.adbtools.deeplinks.DeepLinkData
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.MutableCollectionComboBoxModel
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.LCFlags
import com.intellij.ui.layout.panel
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import icons.AndroidIcons
import org.jetbrains.android.facet.AndroidFacet
import javax.swing.JCheckBox
import javax.swing.SwingConstants
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener

class AdbToolsWindowView(private val project: Project, private val model: AdbToolsModel) : SimpleToolWindowPanel(true, false) {

    private val deviceComboModel = MutableCollectionComboBoxModel<ConnectedAndroidDevice>()
    private val facetComboModel = MutableCollectionComboBoxModel<AndroidFacet>()
    private var debuggerCheckBox: JCheckBox? = null

//    private val deepLinks = DeepLinkParser.getDeepLinks(project)
//    private val openDeepLinkAction = ActionManager.getInstance().getAction("com.github.mwsmith3.adbtools.deeplink")
//    private val deepLinkComboBoxModel = CollectionComboBoxModel(deepLinks)

//    private lateinit var comboBox: ComboBox<ConnectedAndroidDevice>

    private val deviceListCellRenderer = SimpleListCellRenderer.create<ConnectedAndroidDevice>("")
    { it?.name?.replace(Regex("[]]|[\\[]|"), "")?.replace('_', ' ') }

    private val facetListCellRenderer = SimpleListCellRenderer.create<AndroidFacet>("") {
        it.module.name
    }

    private val deviceConnectedContent = panel {
        row {
            comboBox(deviceComboModel, {
                deviceComboModel.selected
            }, {
                deviceComboModel.selectedItem = it
            }, deviceListCellRenderer).constraints(growX)
        }
        row {
            comboBox(facetComboModel, {
                facetComboModel.selected
            }, {
                facetComboModel.selectedItem = it
            }, facetListCellRenderer).constraints(growX)
        }
        row {
            cell { label("") }
            debuggerCheckBox = checkBox("Attach debugger", false).constraints(growX, pushX).component
        }

//            buttonFromAction("DL", ActionPlaces.TOOLBAR, openDeepLinkAction)
//        }.growX
//        row {
//            label("Query parameter:")
//            textField({ queryParam }, { queryParam = it }, 1)
//        }
    }

    private val noDevicesContent = panel(LCFlags.fill) {
        row {
            val emptyLabel = label("No devices connected")
                    .constraints(CCFlags.grow).component
            emptyLabel.icon = AndroidIcons.DeviceExplorer.DevicesLineup
            emptyLabel.horizontalAlignment = SwingConstants.CENTER
            emptyLabel.horizontalTextPosition = SwingConstants.CENTER
            emptyLabel.verticalAlignment = SwingConstants.CENTER
            emptyLabel.verticalTextPosition = SwingConstants.BOTTOM
            emptyLabel.font = JBUI.Fonts.label(13f)
            emptyLabel.foreground = UIUtil.getInactiveTextColor()
        }
    }

    init {
        setContent(false)
        this.toolbar = createActionToolbar("com.github.mwsmith3.adbtools.window.actions").component
        deviceComboModel.addListDataListener(DeviceComboListener())
        model.addStateListener(ModelStateListener())
    }

    private fun setContent(connectedDevices: Boolean) {
        val content = if (connectedDevices) {
            deviceConnectedContent
        } else {
            noDevicesContent
        }
        setContent(content)
    }


//    private fun getSelectedDeepLink() = deepLinkComboBoxModel.selected

    private fun createActionToolbar(actionId: String): ActionToolbar {
        val actionManager = ActionManager.getInstance()
        val actionGroup = actionManager.getAction(actionId) as DefaultActionGroup
        return actionManager.createActionToolbar(ActionPlaces.TOOLWINDOW_CONTENT, actionGroup, true).apply {
            this.setTargetComponent(this@AdbToolsWindowView)
        }
    }

    override fun getData(dataId: String): Any? {
        return when {
            DEVICE_KEY.`is`(dataId) -> {
                deviceComboModel.selected?.device
            }
            FACET_KEY.`is`(dataId) -> {
                facetComboModel.selected
            }
            DEBUGGER_KEY.`is`(dataId) -> {
                debuggerCheckBox?.isSelected
            }
//            DEEP_LINK_KEY.`is`(dataId) -> {
//                getSelectedDeepLink()?.let {
//                    DeepLinkData(it, false, "", "")
//                }
//            }
            else -> {
                super.getData(dataId)
            }
        }
    }

    private inner class ModelStateListener : AdbToolsModel.StateListener {
        override fun deviceAdded(device: ConnectedAndroidDevice) {
            deviceComboModel.addItem(device)
            deviceComboModel.selectedItem = device
        }

        override fun deviceRemoved(device: ConnectedAndroidDevice) {
            deviceComboModel.remove(device)
        }

        override fun deviceUpdated(device: ConnectedAndroidDevice) {
            deviceComboModel.contentsChanged(device)
        }

        override fun devicesCleared() {
            deviceComboModel.removeAll()
        }

        override fun facetsSet(facets: List<AndroidFacet>) {
            facetComboModel.replaceAll(facets)
            if (facets.isNotEmpty()) {
                facetComboModel.selectedItem = facets[0]
            }
        }
    }

    private inner class DeviceComboListener : ListDataListener {
        override fun intervalAdded(e: ListDataEvent) {
            if (e.index0 == 0) {
                setContent(true)
            }
        }
        override fun intervalRemoved(e: ListDataEvent) {
            if (e.index0 == 0) {
                setContent(false)
            }
        }
        override fun contentsChanged(e: ListDataEvent) { }
    }

    companion object {
        val DEVICE_KEY = DataKey.create<IDevice>("device")
        val DEEP_LINK_KEY = DataKey.create<DeepLinkData>("deep link")
        val FACET_KEY = DataKey.create<AndroidFacet>("android facet")
        val DEBUGGER_KEY = DataKey.create<Boolean>("attach debugger")
    }
}