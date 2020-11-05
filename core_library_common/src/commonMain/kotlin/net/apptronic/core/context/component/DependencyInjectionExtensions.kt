package net.apptronic.core.context.component

import net.apptronic.core.context.Contextual
import net.apptronic.core.context.di.DependencyDescriptor
import net.apptronic.core.context.di.Parameters
import net.apptronic.core.context.di.emptyParameters

inline fun <reified TypeDeclaration : Any> Contextual.inject(
        params: Parameters = emptyParameters()
): TypeDeclaration {
    return dependencyProvider.inject(TypeDeclaration::class, params)
}

inline fun <reified TypeDeclaration : Any> Contextual.optional(
        params: Parameters = emptyParameters()
): TypeDeclaration? {
    return dependencyProvider.optional(TypeDeclaration::class, params)
}

inline fun <reified TypeDeclaration : Any> Contextual.injectLazy(
        params: Parameters = emptyParameters()
): Lazy<TypeDeclaration> {
    return dependencyProvider.injectLazy(TypeDeclaration::class, params)
}

inline fun <reified TypeDeclaration : Any> Contextual.optionalLazy(
        params: Parameters = emptyParameters()
): Lazy<TypeDeclaration?> {
    return dependencyProvider.optionalLazy(TypeDeclaration::class, params)
}

fun <TypeDeclaration> Contextual.inject(
        descriptor: DependencyDescriptor<TypeDeclaration>,
        params: Parameters = emptyParameters()
): TypeDeclaration {
    return dependencyProvider.inject(descriptor, params)
}

fun <TypeDeclaration> Contextual.optional(
        descriptor: DependencyDescriptor<TypeDeclaration>,
        params: Parameters = emptyParameters()
): TypeDeclaration? {
    return dependencyProvider.optional(descriptor, params)
}

fun <TypeDeclaration> Contextual.injectLazy(
        descriptor: DependencyDescriptor<TypeDeclaration>,
        params: Parameters = emptyParameters()
): Lazy<TypeDeclaration> {
    return dependencyProvider.injectLazy(descriptor, params)
}

fun <TypeDeclaration> Contextual.optionalLazy(
        descriptor: DependencyDescriptor<TypeDeclaration>,
        params: Parameters = emptyParameters()
): Lazy<TypeDeclaration?> {
    return dependencyProvider.optionalLazy(descriptor, params)
}