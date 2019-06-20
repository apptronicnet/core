package net.apptronic.core.base.utils

import kotlin.reflect.KClass

infix fun Any.isInstanceOf(clazz: KClass<*>): Boolean {
    return clazz.isInstance(this)
}