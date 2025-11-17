package com.newsalert.common

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class LoggerDelegate<T : Any>(
    private val clazz: Class<T>,
) : ReadOnlyProperty<T, Logger> {
    private val logger: Logger = LoggerFactory.getLogger(clazz)

    override fun getValue(
        thisRef: T,
        property: KProperty<*>,
    ): Logger = logger
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Any> logger(): ReadOnlyProperty<T, Logger> = LoggerDelegate(T::class.java)
