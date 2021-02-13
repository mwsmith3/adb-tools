package com.github.mwsmith3.adbtools.util

import com.google.common.util.concurrent.MoreExecutors
import java.util.concurrent.ExecutorService

class ExecutorProviderServiceFake : ExecutorProviderService {

    override fun getTaskExecutorService(): ExecutorService {
        return MoreExecutors.newDirectExecutorService()
    }

    override fun getEdtExecutorService(): ExecutorService {
        return MoreExecutors.newDirectExecutorService()
    }
}
