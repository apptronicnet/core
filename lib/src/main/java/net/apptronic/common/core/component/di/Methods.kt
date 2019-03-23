package net.apptronic.common.core.component.di

/**
 * Method to create instance of Object in provider
 */
internal class BuilderMethod<TypeDeclaration>(
    private val builder: FactoryContext.() -> TypeDeclaration
) {

    fun invoke(context: FactoryContext): TypeDeclaration {
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
internal class RecyclerMethod<TypeDeclaration>(
    private val recycler: (TypeDeclaration) -> Unit
) {

    fun invoke(instance: TypeDeclaration) {
        recycler.invoke(instance)
    }

}