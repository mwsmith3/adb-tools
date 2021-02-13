package com.github.mwsmith3.adbtools.util

import com.google.common.util.concurrent.MoreExecutors
import java.util.concurrent.ExecutorService

class ExecutorProviderServiceFake : ExecutorProviderService {
    override val tasks: ExecutorService = MoreExecutors.newDirectExecutorService()
    override val edt: ExecutorService = MoreExecutors.newDirectExecutorService()
}
