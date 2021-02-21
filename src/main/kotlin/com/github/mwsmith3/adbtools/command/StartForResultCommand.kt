package com.github.mwsmith3.adbtools.command

abstract class StartForResultCommand(protected val packageName: String) : Command<Result>() {

    private val shellOutputPredicate: (String) -> Boolean = {
        it.contains(Regex("(?i)(error)")) || it.contains(Regex("(?i)fail"))
    }

    override fun resolve(): Result {
        val isError = output.find(shellOutputPredicate) != null
        return if (isError) {
            Result.Error(output.joinToString(separator = "\n") { it })
        } else {
            Result.Success
        }
    }
}
