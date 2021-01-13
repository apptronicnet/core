package net.apptronic.core.context.di

import net.apptronic.core.context.Context
import kotlin.reflect.KClass

class ProviderDefinition<TypeDeclaration> internal constructor(
    private val objectDefinition: ObjectDefinition<TypeDeclaration>
) {

    inline fun <reified Mapping : Any> addMapping() {
        addMapping(Mapping::class)
    }

    fun <Mapping : Any> addMapping(
        clazz: KClass<Mapping>
    ) {
        objectDefinition.addMappings(objectKey(clazz))
    }

    fun <Mapping : Any> addMapping(
        descriptor: DependencyDescriptor<Mapping>
    ) {
        objectDefinition.addMappings(objectKey(descriptor))
    }

    internal open fun createProvider(context: Context): ObjectProvider<TypeDeclaration> {
        return objectDefinition.getProvider(context)
    }

}