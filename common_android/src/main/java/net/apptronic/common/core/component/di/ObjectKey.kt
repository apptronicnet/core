package net.apptronic.common.core.component.di

import kotlin.reflect.KClass

data class ObjectKey(
    val clazz: KClass<*>,
    val key: String = ""
)