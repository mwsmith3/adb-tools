package com.github.mwsmith3.adbtools.command.settings

import com.github.mwsmith3.adbtools.command.Command

class GetTalkBackEnabledCommand : Command<Boolean>() {
    override val adbCommand: String
        get() = "settings get secure enabled_accessibility_services"
    override val description: String
        get() = "Get TalkBack enabled"

    override fun resolve() = output[0] == TALKBACK_SETTING

    companion object {
        private const val TALKBACK_SETTING =
            "com.google.android.marvin.talkback/com.google.android.marvin.talkback.TalkBackService"
    }
}
