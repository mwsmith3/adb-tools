package com.github.mwsmith3.adbtools.window

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.device.DeviceProviderService
import com.github.mwsmith3.adbtools.device.DeviceProviderServiceFake
import com.github.mwsmith3.adbtools.util.AndroidFacetProviderService
import com.github.mwsmith3.adbtools.util.AndroidFacetProviderServiceFake
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase
import io.reactivex.rxjava3.core.Observable
import org.jetbrains.android.facet.AndroidFacet
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito

@RunWith(JUnit4::class)
class AdbToolsControllerTest : LightJavaCodeInsightFixtureTestCase() {

    private lateinit var facetProviderServiceFake: AndroidFacetProviderServiceFake
    private lateinit var deviceProviderServiceFake: DeviceProviderServiceFake

    @Before
    fun before() {
        super.setUp()
        facetProviderServiceFake =
            project.getService(AndroidFacetProviderService::class.java) as AndroidFacetProviderServiceFake
        deviceProviderServiceFake = project.getService(DeviceProviderService::class.java) as DeviceProviderServiceFake
    }

    @After
    fun after() {
        super.tearDown()
    }

    @Test
    fun `when no response from device provider service, then model AdbState is Loading`() {
        deviceProviderServiceFake.observable = Observable.never()

        val controller = createController()
        controller.model.test().assertValue { it.adbState is AdbState.Loading }
    }

    @Test
    fun `when no error from device provider service, then model AdbState is Error`() {
        deviceProviderServiceFake.observable = Observable.error(RuntimeException())

        val controller = createController()
        controller.model.test().assertValue { it.adbState is AdbState.Error }
    }

    @Test
    fun `when device provider service emits devices, then model AdbState is Connected with expected ConnectedAndroidDevices`() {
        val device1 = createMockIDevice(0)
        val device2 = createMockIDevice(1)

        deviceProviderServiceFake.observable = Observable.just(listOf(device1), listOf(device2))

        val controller = createController()

        controller.model.test().assertValue {
            val state = it.adbState
            state is AdbState.Connected && state.devices.size == 1 && state.devices[0].second == device2
        }
    }

    @Test
    fun `when no response from facet provider service, then model facets is empty list`() {
        facetProviderServiceFake.observable = Observable.never()

        val controller = createController()
        controller.model.test().assertValue { it.facets.isEmpty() }
    }

    @Test
    fun `when error response from facet provider service, then model facets is empty list`() {
        facetProviderServiceFake.observable = Observable.error(RuntimeException())

        val controller = createController()
        controller.model.test().assertValue { it.facets.isEmpty() }
    }

    @Test
    fun `when facet provider service emits facets, then model facets is expected AndroidFacets`() {
        val facets = listOf(createMockAndroidFacet(), createMockAndroidFacet())
        facetProviderServiceFake.observable = Observable.just(facets)

        val controller = createController()
        controller.model.test().assertValue { it.facets == facets }
    }

    private fun createController() = AdbToolsController(project)

    private fun createMockIDevice(id: Int): IDevice {
        val device: IDevice = Mockito.mock(IDevice::class.java)
        Mockito.`when`(device.serialNumber).thenReturn("mock-serial-number-$id")
        Mockito.`when`(device.isEmulator).thenReturn(false)
        return device
    }

    private fun createMockAndroidFacet() = Mockito.mock(AndroidFacet::class.java)
}
