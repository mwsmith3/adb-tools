package com.github.mwsmith3.adbtools.command

import com.android.ddmlib.IDevice

interface Command<T> {
    val command: String
    val description: String
    fun run(device: IDevice): T
}