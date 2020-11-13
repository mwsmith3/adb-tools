package com.github.mwsmith3.adbtools.util

import org.joor.Reflect

/**
 * Taken from https://github.com/pbreault/adb-idea
 */
inline fun <reified T> on() = Reflect.on(T::class.java)
inline fun <reified T> Reflect.asType() = this.`as`(T::class.java)