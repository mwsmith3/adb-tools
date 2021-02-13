package com.github.mwsmith3.adbtools.util

import java.util.concurrent.ExecutorService

interface ExecutorProviderService {
    val tasks: ExecutorService
    val edt: ExecutorService
}
