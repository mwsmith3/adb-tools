package com.github.mwsmith3.adbtools.window

import com.android.ddmlib.IDevice
import com.android.tools.idea.run.ConnectedAndroidDevice
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.actionSystem.DataKey
import com.intellij.openapi.actionSystem.DefaultActionGroup
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
import javax.swing.JTextField
import javax.swing.SwingConstants
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener

class AdbToolsWindowView(private val project: Project, private val model: AdbToolsModel) :
    SimpleToolWindowPanel(true, false) {

    private val listeners = mutableListOf<AdbToolsWindowViewListener>()
    private val deviceComboModel = MutableCollectionComboBoxModel<ConnectedAndroidDevice>()
    private val facetComboModel = MutableCollectionComboBoxModel<AndroidFacet>()
    private val deepLinkComboModel = MutableCollectionComboBoxModel<String>()
    private lateinit var debuggerCheckBox: JCheckBox
    private val paramsTextField = JTextField("", 1)

    private val deviceListCellRenderer = SimpleListCellRenderer.create<ConnectedAndroidDevice>("") {
        it?.name?.replace(Regex("[]]|[\\[]|"), "")?.replace('_', ' ')
    }

    private val facetListCellRenderer = SimpleListCellRenderer.create<AndroidFacet>("") {
        it.module.name
    }

    private val deviceConnectedContent = panel {
        row("Devices") {
            comboBox(
                deviceComboModel,
                {
                    deviceComboModel.selected
                },
                {
                    deviceComboModel.selectedItem = it
                },
                deviceListCellRenderer
            ).constraints(growX, pushX)
        }
        row("Modules") {
            comboBox(
                facetComboModel,
                {
                    facetComboModel.selected
                },
                { facet ->
                    facetComboModel.selectedItem = facet
                },
                facetListCellRenderer
            ).constraints(growX, pushX).component.addActionListener {
                listeners.forEach {
                    it.onFacetSelected(facetComboModel.selected)
                }
            }
        }
        row("Deep links") {
            cell {
                comboBox(
                    deepLinkComboModel,
                    {
                        deepLinkComboModel.selected
                    },
                    {
                        deepLinkComboModel.selectedItem = it
                    }
                ).constraints(growX, pushX)
            }
            cell {
                paramsTextField(growX, pushX).component.toolTipText = "Deep link query parameters"
            }
        }
//        row {
//            debuggerCheckBox = checkBox("Attach debugger", false).constraints(growX, pushX).component
//            debuggerCheckBox.isEnabled = false
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
            emptyLabel.font = JBUI.Fonts.label(NO_DEVICES_FONT_TEXT_SIZE)
            emptyLabel.foreground = UIUtil.getInactiveTextColor()
        }
    }

    init {
        setContent(false)
        this.toolbar = createActionToolbar("com.github.mwsmith3.adbtools.window.actions").component
        deviceComboModel.addListDataListener(DeviceComboListener())
        model.addStateListener(ModelStateListener())
    }

    fun addListener(listener: AdbToolsWindowViewListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: AdbToolsWindowViewListener) {
        listeners.remove(listener)
    }

    private fun setContent(connectedDevices: Boolean) {
        val content = if (connectedDevices) {
            deviceConnectedContent
        } else {
            noDevicesContent
        }
        setContent(content)
    }

    private fun getSelectedDeepLink(): String? {
        return deepLinkComboModel.selected?.let {
            it + paramsTextField.text
        }
    }

    private fun createActionToolbar(actionId: String): ActionToolbar {
        val actionManager = ActionManager.getInstance()
        val actionGroup = actionManager.getAction(actionId) as DefaultActionGroup
        return actionManager.createActionToolbar(ActionPlaces.TOOLWINDOW_CONTENT, actionGroup, true).apply {
            this.setTargetComponent(this@AdbToolsWindowView)
        }
    }

    override fun getData(dataId: String): Any? {
        return when {
            DEVICE_KEY.`is`(dataId) -> deviceComboModel.selected?.device
            FACET_KEY.`is`(dataId) -> facetComboModel.selected
//            DEBUGGER_KEY.`is`(dataId) -> debuggerCheckBox.isSelected
            DEEP_LINK_KEY.`is`(dataId) -> getSelectedDeepLink()
            else -> super.getData(dataId)
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

        override fun deepLinksSet(deepLinks: List<String>) {
            deepLinkComboModel.replaceAll(deepLinks)
            if (deepLinks.isNotEmpty()) {
                deepLinkComboModel.selectedItem = deepLinks[0]
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

        override fun contentsChanged(e: ListDataEvent) {
            // do nothing
        }
    }

    companion object {
        val DEVICE_KEY = DataKey.create<IDevice>("device")
        val DEEP_LINK_KEY = DataKey.create<String>("deep link")
        val FACET_KEY = DataKey.create<AndroidFacet>("android facet")
//        val DEBUGGER_KEY = DataKey.create<Boolean>("attach debugger")

        private const val NO_DEVICES_FONT_TEXT_SIZE = 13f
    }
}
