package com.github.mwsmith3.adbtools.adb

import com.android.tools.idea.gradle.project.sync.GradleSyncState
import com.github.mwsmith3.adbtools.util.NotificationHelper.info
import com.intellij.openapi.project.Project

object AdbUtil {
    // TODO print adb commands to log

    // TODO check this GradleSyncState
    fun isGradleSyncInProgress(project: Project): Boolean {
        return try {
            GradleSyncState.getInstance(project).isSyncInProgress
        } catch (t: Throwable) {
            info("Couldn't determine if a gradle sync is in progress")
            false
        }
    }
}