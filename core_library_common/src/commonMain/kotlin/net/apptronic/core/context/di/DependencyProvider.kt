package net.apptronic.core.context.di

import kotlin.reflect.KClass

abstract class DependencyProvider {

    inline fun <reified TypeDeclaration : Any> inject(
            params: Parameters = emptyParameters()
    ): TypeDeclaration {
        return inject(TypeDeclaration::class, params)
    }

    inline fun <reified TypeDeclaration : Any> optional(
            params: Parameters = emptyParameters()
    ): TypeDeclaration? {
        return optional(TypeDeclaration::class, params)
    }

    inline fun <reified TypeDeclaration : Any> injectLazy(
            params: Parameters = emptyParameters()
    ): Lazy<TypeDeclaration> {
        return injectLazy(TypeDeclaration::class, params)
    }

    inline fun <reified TypeDeclaration : Any> optionalLazy(
            params: Parameters = emptyParameters()
    ): Lazy<TypeDeclaration?> {
        return optionalLazy(TypeDeclaration::class, params)
    }

    abstract fun <TypeDeclaration : Any> inject(
            clazz: KClass<TypeDeclaration>,
            params: Parameters = emptyParameters()
    ): TypeDeclaration

    abstract fun <TypeDeclaration : Any> optional(
            clazz: KClass<TypeDeclaration>,
            params: Parameters = emptyParameters()
    ): TypeDeclaration?

    abstract fun <TypeDeclaration : Any> injectLazy(
            clazz: KClass<TypeDeclaration>,
            params: Parameters = emptyParameters()
    ): Lazy<TypeDeclaration>

    abstract fun <TypeDeclaration : Any> optionalLazy(
            clazz: KClass<TypeDeclaration>,
            params: Parameters = emptyParameters()
    ): Lazy<TypeDeclaration?>

    abstract fun <TypeDeclaration> inject(
            descriptor: DependencyDescriptor<TypeDeclaration>,
            params: Parameters = emptyParameters()
    ): TypeDeclaration

    abstract fun <TypeDeclaration> optional(
            descriptor: DependencyDescriptor<TypeDeclaration>,
            params: Parameters = emptyParameters()
    ): TypeDeclaration?

    abstract fun <TypeDeclaration> injectLazy(
            descriptor: DependencyDescriptor<TypeDeclaration>,
            params: Parameters = emptyParameters()
    ): Lazy<TypeDeclaration>

    abstract fun <TypeDeclaration> optionalLazy(
            descriptor: DependencyDescriptor<TypeDeclaration>,
            params: Parameters = emptyParameters()
    ): Lazy<TypeDeclaration?>

}