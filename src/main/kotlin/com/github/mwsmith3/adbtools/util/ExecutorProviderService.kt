package com.github.mwsmith3.adbtools.util

import java.util.concurrent.ExecutorService

interface ExecutorProviderService {
    fun getTaskExecutorService(): ExecutorService
    fun getEdtExecutorService(): ExecutorService
}
