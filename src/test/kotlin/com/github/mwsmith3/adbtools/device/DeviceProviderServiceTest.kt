package com.github.mwsmith3.adbtools.device

import com.android.ddmlib.AndroidDebugBridge
import com.android.ddmlib.IDevice
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.MoreExecutors
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
    private lateinit var service: DeviceProviderServiceImpl

    @Before
    fun before() {
        super.setUp()
        service = DeviceProviderServiceImpl(project)
        service.executor = MoreExecutors.newDirectExecutorService()
    }

    @After
    fun after() {
        super.tearDown()
        service.dispose()
    }

    @Test
    fun `when bridge future returns error, then state is Error`() {
        val error = RuntimeException()
        val future = Futures.immediateFailedFuture<AndroidDebugBridge>(error)

        service.setup(future)

        val expectedState = DeviceProviderService.State.Error(error)
        service.observe().test().assertValue(expectedState)
    }

    @Test
    fun `when bridge future returns bridge with devices, then state is Success`() {
        `when`(device.serialNumber).thenReturn("mock-serial-number")
        `when`(device.isEmulator).thenReturn(false)
        `when`(bridge.devices).thenReturn(Array(1) { device })

        val future = Futures.immediateFuture(bridge)
        service.setup(future)

        service.observe().test().assertValue {
            it is DeviceProviderService.State.Success && it.devices.size == 1 && it.devices[0].serial == "mock-serial-number"
        }
    }

//    @Test
//    fun `when dispose, listeners removed from AndroidDebugBridge`() {
//        `when`(bridge.devices).thenReturn(emptyArray())
//
//        val future = Futures.immediateFuture(bridge)
//        service.setup(future)
//
//        service.observe().test()
//        assertEquals(1, AndroidDebugBridge.getDebugBridgeChangeListenerCount())
//        assertEquals(1, AndroidDebugBridge.getDeviceChangeListenerCount())
//
//        service.dispose()
//        assertEquals(0, AndroidDebugBridge.getDebugBridgeChangeListenerCount())
//        assertEquals(0, AndroidDebugBridge.getDeviceChangeListenerCount())
//    }
}
