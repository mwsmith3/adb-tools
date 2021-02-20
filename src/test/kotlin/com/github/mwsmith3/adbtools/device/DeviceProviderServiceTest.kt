package com.github.mwsmith3.adbtools.device

import com.android.ddmlib.AndroidDebugBridge
import com.android.ddmlib.IDevice
import com.google.common.util.concurrent.Futures
import com.intellij.openapi.components.ServiceManager
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.lang.RuntimeException

@RunWith(JUnit4::class)
class DeviceProviderServiceTest : LightJavaCodeInsightFixtureTestCase() {

    private val bridge: AndroidDebugBridge = Mockito.mock(AndroidDebugBridge::class.java)
    private val device: IDevice = Mockito.mock(IDevice::class.java)
    private lateinit var bridgeProviderService: BridgeProviderServiceFake
    private lateinit var service: DeviceProviderServiceImpl

    @Before
    fun before() {
        super.setUp()
        bridgeProviderService = ServiceManager.getService(BridgeProviderService::class.java) as BridgeProviderServiceFake
    }

    @After
    fun after() {
        super.tearDown()
        service.dispose()
        bridgeProviderService.provider = null
    }

    @Test
    fun `when bridge future returns error, then error emitted`() {
        val error = RuntimeException()
        bridgeProviderService.provider = { _ -> Futures.immediateFailedFuture(error) }
        service = DeviceProviderServiceImpl(project)

        service.observe().test().assertError(error)
    }

    @Test
    fun `when bridge future returns bridge with devices, then state is Success`() {
        `when`(device.serialNumber).thenReturn("mock-serial-number")
        `when`(device.isEmulator).thenReturn(false)
        `when`(bridge.devices).thenReturn(Array(1) { device })

        bridgeProviderService.provider = { _ -> Futures.immediateFuture(bridge) }
        service = DeviceProviderServiceImpl(project)

        service.observe().test().assertValue {
            it.size == 1 && it[0].serialNumber == "mock-serial-number"
        }
    }
}
