package net.apptronic.core.component

import net.apptronic.core.component.di.Descriptor
import net.apptronic.core.component.di.Parameters
import net.apptronic.core.component.di.emptyParameters
import kotlin.reflect.KClass

fun <TypeDeclaration : Any> Component.inject(
        clazz: KClass<TypeDeclaration>,
        params: Parameters = emptyParameters()
): TypeDeclaration {
    return provider().inject(clazz, params)
}

fun <TypeDeclaration : Any> Component.optional(
        clazz: KClass<TypeDeclaration>,
        params: Parameters = emptyParameters()
): TypeDeclaration? {
    return provider().optional(clazz, params)
}

fun <TypeDeclaration : Any> Component.injectLazy(
        clazz: KClass<TypeDeclaration>,
        params: Parameters = emptyParameters()
): Lazy<TypeDeclaration> {
    return provider().injectLazy(clazz, params)
}

fun <TypeDeclaration : Any> Component.optionalLazy(
        clazz: KClass<TypeDeclaration>,
        params: Parameters = emptyParameters()
): Lazy<TypeDeclaration?> {
    return provider().optionalLazy(clazz, params)
}

fun <TypeDeclaration> Component.inject(
        descriptor: Descriptor<TypeDeclaration>,
        params: Parameters = emptyParameters()
): TypeDeclaration {
    return provider().inject(descriptor, params)
}

fun <TypeDeclaration> Component.optional(
        descriptor: Descriptor<TypeDeclaration>,
        params: Parameters = emptyParameters()
): TypeDeclaration? {
    return provider().optional(descriptor, params)
}

fun <TypeDeclaration> Component.injectLazy(
        descriptor: Descriptor<TypeDeclaration>,
        params: Parameters = emptyParameters()
): Lazy<TypeDeclaration> {
    return provider().injectLazy(descriptor, params)
}

fun <TypeDeclaration> Component.optionalLazy(
        descriptor: Descriptor<TypeDeclaration>,
        params: Parameters = emptyParameters()
): Lazy<TypeDeclaration?> {
    return provider().optionalLazy(descriptor, params)
}