package net.apptronic.common.core.component.di

import net.apptronic.common.core.component.ComponentContext
import net.apptronic.common.core.component.lifecycle.LifecycleStage
import kotlin.reflect.KClass

class DIContext(
    private val context: ComponentContext,
    private val parent: DIContext?
) {

    private val objects = mutableMapOf<ObjectKey, Any>()
    private val modules = mutableListOf<Module>()

    inline fun <reified TypeDeclaration : Any> addInstance(
        instance: TypeDeclaration,
        name: String = ""
    ) {
        addInstance(instance, TypeDeclaration::class, name)
    }

    fun <TypeDeclaration : Any> addInstance(
        instance: TypeDeclaration, clazz:
        KClass<TypeDeclaration>,
        name: String = ""
    ) {
        val key = ObjectKey(clazz, name)
        objects[key] = instance
    }

    fun addModule(moduleDefinition: ModuleDefinition) {
        modules.add(moduleDefinition.buildInstance(this))
    }

    inline fun <reified TypeDeclaration : Any> get(
        name: String = "",
        params: Parameters = parameters { }
    ): TypeDeclaration {
        return get(TypeDeclaration::class, name, params)
    }

    fun <TypeDeclaration : Any> get(
        clazz: KClass<TypeDeclaration>,
        name: String = "",
        params: Parameters = parameters { }
    ): TypeDeclaration {
        val key = ObjectKey(clazz, name)
        return getInstanceInternal(key, params)
    }

    private fun <TypeDeclaration : Any> getInstanceInternal(
        key: ObjectKey,
        params: Parameters
    ): TypeDeclaration {
        val localLifecycleStage = context.getLifecycle().getActiveStage()
            ?: throw IllegalStateException("Lifecycle was terminated")
        return searchInstance(localLifecycleStage, key, params)
    }

    private fun <TypeDeclaration : Any> searchInstance(
        callerLifecycleStage: LifecycleStage,
        key: ObjectKey,
        params: Parameters
    ): TypeDeclaration {
        val localLifecycleStage = context.getLifecycle().getActiveStage()
            ?: throw IllegalStateException("Lifecycle was terminated")
        val local: TypeDeclaration? =
            obtainLocalInstance(callerLifecycleStage, localLifecycleStage, key, params)
        if (local != null) {
            return local
        }
        val parental: TypeDeclaration? = parent?.searchInstance(callerLifecycleStage, key, params)
        if (parental != null) {
            return parental
        }
        throw ObjectNotFoundException("Object $key or it's factory/cast is not found in current context and all parental contexts")
    }

    private fun <TypeDeclaration : Any> obtainLocalInstance(
        callerLifecycleStage: LifecycleStage,
        localLifecycleStage: LifecycleStage,
        objectKey: ObjectKey,
        params: Parameters
    ): TypeDeclaration? {
        val obj = objects[objectKey]
        if (obj != null) {
            return obj as TypeDeclaration
        }
        modules.forEach { module ->
            val providerSearch = module.getProvider(objectKey)
            if (providerSearch != null) {
                val provider = providerSearch as ObjectProvider<TypeDeclaration>
                val factoryContext = FactoryContext(
                    this,
                    params,
                    localLifecycleStage,
                    callerLifecycleStage
                )
                return@obtainLocalInstance provider.provide(factoryContext)
            }
        }
        return null
    }

}