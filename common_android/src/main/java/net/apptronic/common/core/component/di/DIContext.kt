package net.apptronic.common.core.component.di

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import net.apptronic.common.core.component.ComponentContext
import kotlin.reflect.KClass

class DIContext(
    private val context: ComponentContext,
    private val parent: DIContext?
) {

    private val factoryContext = FactoryContext(this)

    private val objects = mutableMapOf<ObjectKey, Any>()
    private val modules = mutableListOf<Module>()

    inline fun <reified T : Any> addInstance(instance: T, name: String = "") {
        addInstance(instance, T::class, name)
    }

    fun <T : Any> addInstance(instance: T, clazz: KClass<*>, name: String = "") {
        val key = ObjectKey(clazz, name)
        objects[key] = instance
    }

    fun addModule(moduleDefinition: ModuleDefinition) {
        modules.add(moduleDefinition.buildInstance(this))
    }

    inline fun <reified T : Any> get(name: String = ""): T {
        return get(T::class, name)
    }

    fun <T : Any> get(clazz: KClass<*>, name: String = ""): T {
        val key = ObjectKey(clazz, name)
        return get(key)
    }

    private fun <T : Any> get(key: ObjectKey): T {
        val local: T? = obtainLocalInstance(key)
        if (local != null) {
            return local
        }
        if (parent != null) {
            return parent.get(key)
        }
        throw ObjectIsNotFoundException()
    }

    private fun <T : Any> obtainLocalInstance(key: ObjectKey): T? {
        val obj = objects[key]
        if (obj != null) {
            return obj as T
        }
        modules.forEach { module ->
            val providerSearch = module.getProvider(key)
            if (providerSearch != null) {
                val provider = providerSearch as ObjectProvider<T>
                val instance = provider.provide(factoryContext)
                context.getLifecycle().getActiveStage()?.doOnExit {
                    provider.recycler.invoke(instance)
                }
                return@obtainLocalInstance instance
            }
        }
        return null
    }

}