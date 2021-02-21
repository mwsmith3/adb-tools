package com.github.mwsmith3.adbtools.command

class GetPackageInstalledCommand(private val packageName: String) : Command<Boolean>() {
    override val adbCommand: String
        get() = "pm list packages $packageName"
    override val description: String
        get() = "Get Package Installed"

    override fun resolve(): Boolean {
        return output.contains("package:$packageName")
    }
}
