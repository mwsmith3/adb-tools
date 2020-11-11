package com.github.mwsmith3.adbtools.actions

import com.github.mwsmith3.adbtools.actions.adbexecutable.TalkBackAdbExecutable

class TalkbackAction : AdbAction() {
    override fun getAdbExecutable() = TalkBackAdbExecutable
}