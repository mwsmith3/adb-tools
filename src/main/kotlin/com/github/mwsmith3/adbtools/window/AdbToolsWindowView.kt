package com.github.mwsmith3.adbtools.window

import com.android.ddmlib.IDevice
import com.android.tools.idea.run.ConnectedAndroidDevice
import com.github.mwsmith3.adbtools.util.ExecutorProviderService
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.actionSystem.DataKey
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.MutableCollectionComboBoxModel
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.LCFlags
import com.intellij.ui.layout.panel
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import icons.AndroidIcons
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.jetbrains.android.facet.AndroidFacet
import javax.swing.JComboBox
import javax.swing.SwingConstants
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener

class AdbToolsWindowView(private val model: Observable<AdbToolsModel>) :
    SimpleToolWindowPanel(true, false), Disposable {

    private val executorProvider = ServiceManager.getService(ExecutorProviderService::class.java)

    private val disposables = CompositeDisposable()

    private val deviceComboModel = MutableCollectionComboBoxModel<ConnectedAndroidDevice>()

    private val facetComboModel = MutableCollectionComboBoxModel<AndroidFacet>()
    private lateinit var facetComboComponent: JComboBox<AndroidFacet>

    private val deviceListCellRenderer = SimpleListCellRenderer.create<ConnectedAndroidDevice>("") {
        it?.serial?.replace(Regex("[]]|[\\[]|"), "")?.replace('_', ' ')
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
            facetComboComponent = comboBox(
                facetComboModel,
                {
                    facetComboModel.selected
                },
                { facet ->
                    facetComboModel.selectedItem = facet
                },
                facetListCellRenderer
            ).constraints(growX, pushX).component
        }
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
        observeModel()
    }

    private fun observeModel() {
        disposables.add(
            model
                .subscribeOn(Schedulers.from(executorProvider.edt))
                .subscribe(
                    {
                        setAdbState(it.adbState)
                        setFacets(it.facets)
                    },
                    {
                        Logger.getInstance(AdbToolsWindowView::class.java).error(it)
                        showErrorView()
                    }
                )
        )
    }

    private fun setAdbState(state: AdbState) {
        when (state) {
            is AdbState.Loading -> {
                showLoadingView()
            }
            is AdbState.Error -> {
                showErrorView()
            }
            is AdbState.Connected -> {
                setDevices(state.devices)
            }
        }
    }

    private fun setContent(connectedDevices: Boolean) {
        val content = if (connectedDevices) {
            deviceConnectedContent
        } else {
            noDevicesContent
        }
        setContent(content)
    }

    private fun createActionToolbar(actionId: String): ActionToolbar {
        val actionManager = ActionManager.getInstance()
        val actionGroup = actionManager.getAction(actionId) as DefaultActionGroup
        return actionManager.createActionToolbar(ActionPlaces.TOOLWINDOW_CONTENT, actionGroup, true).apply {
            this.setTargetComponent(this@AdbToolsWindowView)
        }
    }

    private fun showErrorView() {
//        throw NotImplementedError()
    }

    private fun showLoadingView() {
//        throw NotImplementedError()
    }

    private fun setDevices(devices: List<ConnectedAndroidDevice>) {
        val newSelection = if (devices.isNotEmpty()) {
            val selected = deviceComboModel.selected
            devices.find { selected?.serial == it.serial } ?: devices[0]
        } else {
            null
        }
        deviceComboModel.replaceAll(devices)
        newSelection?.let {
            deviceComboModel.selectedItem = it
        }
    }

    private fun setFacets(facets: List<AndroidFacet>) {
        if (facetComboModel.items == facets) return
        facetComboModel.replaceAll(facets)
        if (facets.isNotEmpty()) {
            facetComboModel.selectedItem = facets[0]
        }
    }

    override fun getData(dataId: String): Any? {
        return when {
            DEVICE_KEY.`is`(dataId) -> deviceComboModel.selected?.device
            FACET_KEY.`is`(dataId) -> facetComboModel.selected
            else -> super.getData(dataId)
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
        val FACET_KEY = DataKey.create<AndroidFacet>("android facet")
        private const val NO_DEVICES_FONT_TEXT_SIZE = 13f
    }

    override fun dispose() {
        disposables.clear()
    }
}
