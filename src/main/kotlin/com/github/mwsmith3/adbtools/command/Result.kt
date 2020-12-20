package com.github.mwsmith3.adbtools.command

sealed class Result {
    object Success : Result()
    data class Error(val message: String) : Result()
}