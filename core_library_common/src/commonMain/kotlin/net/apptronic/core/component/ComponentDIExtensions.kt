package net.apptronic.core.component

import net.apptronic.core.component.di.Descriptor
import net.apptronic.core.component.di.Parameters
import net.apptronic.core.component.di.emptyParameters
import kotlin.reflect.KClass

inline fun <reified TypeDeclaration : Any> Component.inject(
        params: Parameters = emptyParameters()
): TypeDeclaration {
    return provider().inject(TypeDeclaration::class, params)
}

inline fun <reified TypeDeclaration : Any> Component.optional(
        params: Parameters = emptyParameters()
): TypeDeclaration? {
    return provider().optional(TypeDeclaration::class, params)
}

inline fun <reified TypeDeclaration : Any> Component.injectLazy(
        params: Parameters = emptyParameters()
): Lazy<TypeDeclaration> {
    return provider().injectLazy(TypeDeclaration::class, params)
}

inline fun <reified TypeDeclaration : Any> Component.optionalLazy(
        params: Parameters = emptyParameters()
): Lazy<TypeDeclaration?> {
    return provider().optionalLazy(TypeDeclaration::class, params)
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