package com.github.mwsmith3.adbtools.util

import com.android.ddmlib.IDevice
import com.android.tools.idea.run.activity.ActivityLocator
import com.android.tools.idea.run.activity.DefaultActivityLocator
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.ThrowableComputable
import org.jetbrains.android.facet.AndroidFacet

/**
 * Taken from https://github.com/pbreault/adb-idea
 */
fun waitUntil(timeoutMillis: Long = 30000L, step: Long = 100L, condition: () -> Boolean) {
    val endTime = System.currentTimeMillis() + timeoutMillis
    while (System.currentTimeMillis() < endTime) {
        if (condition()) {
            return
        }
        Thread.sleep(step)
    }
}

@Throws(ActivityLocator.ActivityLocatorException::class)
fun getDefaultActivityName(facet: AndroidFacet, device: IDevice): String {
    return ApplicationManager.getApplication()
            .runReadAction(ThrowableComputable<String, ActivityLocator.ActivityLocatorException?> { DefaultActivityLocator(facet).getQualifiedActivityName(device) })
}