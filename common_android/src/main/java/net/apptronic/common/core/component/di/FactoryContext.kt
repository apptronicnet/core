package net.apptronic.common.core.component.di

import net.apptronic.common.core.component.lifecycle.LifecycleStage
import kotlin.reflect.KClass

class FactoryContext(
    private val context: DIContext,
    private val parameters: Parameters,
    internal val localLifecycleStage: LifecycleStage,
    internal val callerLifecycleStage: LifecycleStage
) {

    inline fun <reified ObjectType : Any> inject(
        name: String = ""
    ): ObjectType {
        return inject(ObjectType::class, name)
    }

    inline fun <reified ObjectType : Any> injectLazy(
        name: String = ""
    ): Lazy<ObjectType> {
        return injectLazy(ObjectType::class, name)
    }

    fun <ObjectType : Any> injectLazy(
        clazz: KClass<ObjectType>,
        name: String = ""
    ): Lazy<ObjectType> {
        return lazy {
            inject(clazz, name)
        }
    }

    fun <ObjectType : Any> inject(
        clazz: KClass<ObjectType>,
        name: String = ""
    ): ObjectType {
        return parameters.get(clazz, name) ?: context.get(clazz, name)
    }

}