package net.apptronic.core.component.di

import kotlin.reflect.KClass

inline fun <reified TypeDeclaration : Any> DependencyProvider.inject(
        params: Parameters = emptyParameters()
): TypeDeclaration {
    return inject(TypeDeclaration::class, params)
}

inline fun <reified TypeDeclaration : Any> DependencyProvider.optional(
        params: Parameters = emptyParameters()
): TypeDeclaration? {
    return optional(TypeDeclaration::class, params)
}

inline fun <reified TypeDeclaration : Any> DependencyProvider.injectLazy(
        params: Parameters = emptyParameters()
): Lazy<TypeDeclaration> {
    return injectLazy(TypeDeclaration::class, params)
}

inline fun <reified TypeDeclaration : Any> DependencyProvider.optionalLazy(
        params: Parameters = emptyParameters()
): Lazy<TypeDeclaration?> {
    return optionalLazy(TypeDeclaration::class, params)
}

interface DependencyProvider {

    fun <TypeDeclaration : Any> inject(
            clazz: KClass<TypeDeclaration>,
            params: Parameters = emptyParameters()
    ): TypeDeclaration

    fun <TypeDeclaration : Any> optional(
            clazz: KClass<TypeDeclaration>,
            params: Parameters = emptyParameters()
    ): TypeDeclaration?

    fun <TypeDeclaration : Any> injectLazy(
            clazz: KClass<TypeDeclaration>,
            params: Parameters = emptyParameters()
    ): Lazy<TypeDeclaration>

    fun <TypeDeclaration : Any> optionalLazy(
            clazz: KClass<TypeDeclaration>,
            params: Parameters = emptyParameters()
    ): Lazy<TypeDeclaration?>

    fun <TypeDeclaration> inject(
            descriptor: DependencyDescriptor<TypeDeclaration>,
            params: Parameters = emptyParameters()
    ): TypeDeclaration

    fun <TypeDeclaration> optional(
            descriptor: DependencyDescriptor<TypeDeclaration>,
            params: Parameters = emptyParameters()
    ): TypeDeclaration?

    fun <TypeDeclaration> injectLazy(
            descriptor: DependencyDescriptor<TypeDeclaration>,
            params: Parameters = emptyParameters()
    ): Lazy<TypeDeclaration>

    fun <TypeDeclaration> optionalLazy(
            descriptor: DependencyDescriptor<TypeDeclaration>,
            params: Parameters = emptyParameters()
    ): Lazy<TypeDeclaration?>

}