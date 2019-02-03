package net.apptronic.common.core.component.di

import kotlin.reflect.KClass

/**
 * Unique key for object in DI context
 */
data class ObjectKey(
    val clazz: KClass<*>,
    val name: String = ""
)