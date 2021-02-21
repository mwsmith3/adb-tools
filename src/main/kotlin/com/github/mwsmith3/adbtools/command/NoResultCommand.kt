package com.github.mwsmith3.adbtools.command

abstract class NoResultCommand : Command<Unit>() {
    override fun resolve() {
        return
    }
}
