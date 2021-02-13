package com.github.mwsmith3.adbtools.util

import com.intellij.util.concurrency.EdtExecutorService
import org.jetbrains.ide.PooledThreadExecutor
import java.util.concurrent.ExecutorService

class ExecutorProviderServiceImpl : ExecutorProviderService {
    override fun getTaskExecutorService(): ExecutorService {
        return PooledThreadExecutor.INSTANCE
    }

    override fun getEdtExecutorService(): ExecutorService {
        return EdtExecutorService.getInstance()
    }
}
