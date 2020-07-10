package net.apptronic.core.component

import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.di.DependencyDescriptor
import net.apptronic.core.component.di.DependencyProvider
import net.apptronic.core.component.di.Parameters
import net.apptronic.core.component.di.emptyParameters

fun Contextual.provider(): DependencyProvider = context.dependencyDispatcher

inline fun <reified TypeDeclaration : Any> Contextual.inject(
        params: Parameters = emptyParameters()
): TypeDeclaration {
    return provider().inject(TypeDeclaration::class, params)
}

inline fun <reified TypeDeclaration : Any> Contextual.optional(
        params: Parameters = emptyParameters()
): TypeDeclaration? {
    return provider().optional(TypeDeclaration::class, params)
}

inline fun <reified TypeDeclaration : Any> Contextual.injectLazy(
        params: Parameters = emptyParameters()
): Lazy<TypeDeclaration> {
    return provider().injectLazy(TypeDeclaration::class, params)
}

inline fun <reified TypeDeclaration : Any> Contextual.optionalLazy(
        params: Parameters = emptyParameters()
): Lazy<TypeDeclaration?> {
    return provider().optionalLazy(TypeDeclaration::class, params)
}

fun <TypeDeclaration> Contextual.inject(
        descriptor: DependencyDescriptor<TypeDeclaration>,
        params: Parameters = emptyParameters()
): TypeDeclaration {
    return provider().inject(descriptor, params)
}

fun <TypeDeclaration> Contextual.optional(
        descriptor: DependencyDescriptor<TypeDeclaration>,
        params: Parameters = emptyParameters()
): TypeDeclaration? {
    return provider().optional(descriptor, params)
}

fun <TypeDeclaration> Contextual.injectLazy(
        descriptor: DependencyDescriptor<TypeDeclaration>,
        params: Parameters = emptyParameters()
): Lazy<TypeDeclaration> {
    return provider().injectLazy(descriptor, params)
}

fun <TypeDeclaration> Contextual.optionalLazy(
        descriptor: DependencyDescriptor<TypeDeclaration>,
        params: Parameters = emptyParameters()
): Lazy<TypeDeclaration?> {
    return provider().optionalLazy(descriptor, params)
}