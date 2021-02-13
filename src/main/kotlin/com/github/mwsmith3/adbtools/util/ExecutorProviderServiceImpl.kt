package com.github.mwsmith3.adbtools.util

import com.intellij.util.concurrency.EdtExecutorService
import org.jetbrains.ide.PooledThreadExecutor
import java.util.concurrent.ExecutorService

class ExecutorProviderServiceImpl : ExecutorProviderService {
    override val tasks: ExecutorService = PooledThreadExecutor.INSTANCE
    override val edt: ExecutorService = EdtExecutorService.getInstance()
}
