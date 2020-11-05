package net.apptronic.core.context.di

import net.apptronic.core.context.Context

/**
 * Method to create instance of Object in provider
 */
internal class BuilderMethod<TypeDeclaration, BuilderContext : Scope>(
        private val builder: BuilderContext.() -> TypeDeclaration
) {

    fun invoke(context: BuilderContext): TypeDeclaration {
        return builder.invoke(context)
    }

}

/**
 * Method to create factory of object providers
 */
internal class ProviderFactoryMethod<TypeDeclaration>(
        private val providerFactory: (Context) -> ObjectProvider<TypeDeclaration>
) {

    fun invoke(context: Context): ObjectProvider<TypeDeclaration> {
        return providerFactory.invoke(context)
    }

}

/**
 * Method to recycle object instance
 */
internal class RecyclerMethod<T>(
        private val target: T,
        private val onRecycle: (T) -> Unit
) {

    fun executeRecycle() {
        onRecycle(target)
    }

}