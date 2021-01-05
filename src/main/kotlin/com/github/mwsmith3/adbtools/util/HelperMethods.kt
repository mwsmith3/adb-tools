package com.github.mwsmith3.adbtools.util

import com.android.ddmlib.IDevice
import com.android.tools.idea.run.activity.ActivityLocator
import com.android.tools.idea.run.activity.DefaultActivityLocator
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.ThrowableComputable
import org.jetbrains.android.facet.AndroidFacet

@Throws(ActivityLocator.ActivityLocatorException::class)
fun getDefaultActivityName(facet: AndroidFacet, device: IDevice): String {
    return ApplicationManager.getApplication()
        .runReadAction(ThrowableComputable<String, ActivityLocator.ActivityLocatorException?> {
            DefaultActivityLocator(
                facet
            ).getQualifiedActivityName(device)
        })
}
