package com.github.mwsmith3.adbtools.command.settings

import com.github.mwsmith3.adbtools.command.NoResultCommand

class ToggleTalkBackCommand(private val enabled: Boolean) : NoResultCommand() {

    override val adbCommand: String
        get() = if (enabled) {
            PUT_TALKBACK_COMMAND + TALKBACK_OFF_SETTING
        } else {
            PUT_TALKBACK_COMMAND + TALKBACK_ON_SETTING
        }

    override val description: String
        get() = "Toggle TalkBack status"

    companion object {
        private const val PUT_TALKBACK_COMMAND = "settings put secure enabled_accessibility_services "
        private const val TALKBACK_OFF_SETTING =
            "com.android.talkback/com.google.android.marvin.talkback.TalkBackService"
        private const val TALKBACK_ON_SETTING =
            "com.google.android.marvin.talkback/com.google.android.marvin.talkback.TalkBackService"
    }
}
