package com.github.mwsmith3.adbtools.util

import com.android.ddmlib.Client
import com.android.ddmlib.IDevice
import com.android.tools.idea.run.AndroidProcessHandler
import com.android.tools.idea.run.editor.AndroidDebugger
import com.intellij.execution.ExecutionManager
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessOutputTypes
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import org.joor.Reflect.on

/**
 * Taken from https://github.com/pbreault/adb-idea
 */
class Debugger(private val project: Project, private val device: IDevice, private val packageName: String) {

    fun attach() {
        var client: Client? = null
        waitUntil {
            client = device.getClient(packageName)
            AndroidDebugger.EP_NAME.extensions.isNotEmpty() && client != null
        }
        for (androidDebugger in AndroidDebugger.EP_NAME.extensions) {
            if (androidDebugger.supportsProject(project)) {
                ApplicationManager.getApplication().invokeLater { closeOldSessionAndRun(androidDebugger, device.getClient(packageName) ?: client!!) }
                break
            }
        }
    }

    private fun closeOldSessionAndRun(androidDebugger: AndroidDebugger<*>, client: Client) {
        terminateRunSessions(client)
        AttachToClient(androidDebugger, project, client).get()
    }

    // Disconnect any active run sessions to the same client
    private fun terminateRunSessions(selectedClient: Client) {
        TerminateRunSession(selectedClient, project).get()
    }
}

class TerminateRunSession(
        private val selectedClient: Client,
        private val project: Project
) : BackwardCompatibleGetter<Unit>() {
    override fun getCurrentImplementation() {
        val pid = selectedClient.clientData.pid
        // find if there are any active run sessions to the same client, and terminate them if so
        for (handler in ExecutionManager.getInstance(project).getRunningProcesses()) {
            if (handler is AndroidProcessHandler) {
                val client = handler.getClient(selectedClient.device)
                if (client != null && client.clientData.pid == pid) {
                    handler.detachProcess()
                    handler.notifyTextAvailable("Disconnecting run session: a new debug session will be established.\n", ProcessOutputTypes.STDOUT)
                    break
                }
            }
        }
    }

    override fun getPreviousImplementation() {
        val pid = pidFrom(selectedClient)
        // find if there are any active run sessions to the same client, and terminate them if so
        for (handler in RunningProcessesGetter(project).get()) {
            if (handler is AndroidProcessHandler) {
                val device = on(selectedClient).call("getDevice").get<IDevice>()
                val client = handler.getClient(device)
                if (client != null && pidFrom(client) == pid) {
                    handler.detachProcess()
                    handler.notifyTextAvailable("Disconnecting run session: a new debug session will be established.\n", ProcessOutputTypes.STDOUT)
                    break
                }
            }
        }

    }

    private fun pidFrom(client: Client) = on(client).call("getClientData").call("getPid").get<Int>()!!
}

class AttachToClient(private val androidDebugger: AndroidDebugger<*>,
                     private val project: Project,
                     private val client: Client) : BackwardCompatibleGetter<Unit>() {
    override fun getCurrentImplementation() {
        androidDebugger.attachToClient(project, client)
    }

    override fun getPreviousImplementation() {
        on(androidDebugger).call("attachToClient", project, client)
    }
}

private class RunningProcessesGetter(
        val project: Project
) : BackwardCompatibleGetter<Array<ProcessHandler>>() {
    override fun getCurrentImplementation(): Array<ProcessHandler> {
        return ExecutionManager.getInstance(project).getRunningProcesses()
    }

    override fun getPreviousImplementation(): Array<ProcessHandler> {
        return on<ExecutionManager>().call("getInstance", project).call("getRunningProcesses").get<Array<ProcessHandler>>()
    }
}
