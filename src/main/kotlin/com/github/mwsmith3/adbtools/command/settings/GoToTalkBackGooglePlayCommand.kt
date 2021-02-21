package com.github.mwsmith3.adbtools.command.settings

import com.github.mwsmith3.adbtools.command.NoResultCommand

class GoToTalkBackGooglePlayCommand : NoResultCommand() {
    override val adbCommand: String
        get() = "am start -a android.intent.action.VIEW -d 'market://details?id=com.google.android.marvin.talkback'"
    override val description: String
        get() = "Navigate to GooglePlay TalkBack install"
}
