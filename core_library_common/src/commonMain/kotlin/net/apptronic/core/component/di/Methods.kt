package net.apptronic.core.component.di

/**
 * Method to create instance of Object in provider
 */
internal class BuilderMethod<TypeDeclaration, BuilderContext : ObjectBuilderScope>(
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
        private val providerFactory: () -> ObjectProvider<TypeDeclaration>
) {

    fun invoke(): ObjectProvider<TypeDeclaration> {
        return providerFactory.invoke()
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