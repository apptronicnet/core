package net.apptronic.core.context.di

import net.apptronic.core.context.Context

internal sealed class ObjectProvider<TypeDeclaration>(
    objectKey: ObjectKey
) {

    abstract val typeName: String

    private val objectKeys = mutableListOf<ObjectKey>()

    internal fun addMapping(additionalKeys: Iterable<ObjectKey>) {
        objectKeys.addAll(additionalKeys)
    }

    internal fun getMappings(): List<ObjectKey> {
        return objectKeys
    }

    init {
        objectKeys.add(objectKey)
    }

    internal fun isMatch(key: ObjectKey): Boolean {
        return objectKeys.any { it == key }
    }

    open fun onModuleLoaded(definitionContext: Context) {
        // implement by subclasses if needed
    }

    abstract fun provide(
        definitionContext: Context,
        dispatcher: DependencyDispatcher,
        searchSpec: SearchSpec
    ): TypeDeclaration

}

internal abstract class ObjectBuilderProvider<TypeDeclaration : Any, BuilderScope : Scope> internal constructor(
    objectKey: ObjectKey,
    internal val builder: BuilderMethod<TypeDeclaration, BuilderScope>
) : ObjectProvider<TypeDeclaration>(objectKey) {

}

internal fun <TypeDeclaration : Any> singleProvider(
    objectKey: ObjectKey,
    builder: BuilderMethod<TypeDeclaration, SingleScope>,
    initOnLoad: Boolean
): ObjectProvider<TypeDeclaration> {
    return SingleProvider(objectKey, builder, initOnLoad)
}


internal fun <TypeDeclaration : Any> factoryProvider(
    objectKey: ObjectKey,
    builder: BuilderMethod<TypeDeclaration, FactoryScope>
): ObjectProvider<TypeDeclaration> {
    return FactoryProvider(objectKey, builder)
}

internal fun <TypeDeclaration : Any> bindProvider(
    objectKey: ObjectKey
): ObjectProvider<TypeDeclaration> {
    return BindProvider(objectKey)
}

internal fun <TypeDeclaration : Any> instanceProvider(
    objectKey: ObjectKey, instance: TypeDeclaration
): ObjectProvider<TypeDeclaration> {
    return InstanceProvider(objectKey, instance)
}

private class SingleProvider<TypeDeclaration : Any>(
    objectKey: ObjectKey,
    builder: BuilderMethod<TypeDeclaration, SingleScope>,
    val initOnLoad: Boolean
) : ObjectBuilderProvider<TypeDeclaration, SingleScope>(objectKey, builder) {

    override val typeName: String = "single"

    private var entity: TypeDeclaration? = null

    override fun onModuleLoaded(definitionContext: Context) {
        super.onModuleLoaded(definitionContext)
        if (initOnLoad) {
            createInstance(definitionContext, definitionContext.dependencyDispatcher)
        }
    }

    override fun provide(
        definitionContext: Context,
        dispatcher: DependencyDispatcher,
        searchSpec: SearchSpec
    ): TypeDeclaration {
        return entity ?: createInstance(definitionContext, dispatcher)
    }

    private fun createInstance(
        definitionContext: Context,
        dispatcher: DependencyDispatcher
    ): TypeDeclaration {
        val scope = SingleScope(definitionContext, dispatcher)
        val instance: TypeDeclaration = builder.invoke(scope)
        scope.autoRecycle(instance)
        val processed = definitionContext.plugins.nextProvide(definitionContext, instance)
        entity = processed
        definitionContext.lifecycle.doOnTerminate {
            scope.finalize()
            entity = null
        }
        return processed
    }

}

private class FactoryProvider<TypeDeclaration : Any>(
    objectKey: ObjectKey,
    builder: BuilderMethod<TypeDeclaration, FactoryScope>
) : ObjectBuilderProvider<TypeDeclaration, FactoryScope>(objectKey, builder) {

    override val typeName: String = "factory"

    override fun provide(
        definitionContext: Context,
        dispatcher: DependencyDispatcher,
        searchSpec: SearchSpec
    ): TypeDeclaration {
        val injectionContext = searchSpec.context
        val scope = FactoryScope(definitionContext, injectionContext, dispatcher, searchSpec.params)
        val instance = builder.invoke(scope)
        scope.autoRecycle(instance)
        /**
         * Factory instance recycled when on exit from caller stage
         */
        injectionContext.lifecycle.onExitFromActiveStage {
            scope.finalize()
        }
        return definitionContext.plugins.nextProvide(definitionContext, instance)
    }

}


private class BindProvider<TypeDeclaration>(
    private val objectKey: ObjectKey
) : ObjectProvider<TypeDeclaration>(objectKey) {

    override val typeName: String = "bind"

    override fun provide(
        definitionContext: Context,
        dispatcher: DependencyDispatcher,
        searchSpec: SearchSpec
    ): TypeDeclaration {
        return dispatcher.inject(objectKey, emptyParameters()) as TypeDeclaration
    }

}

private class InstanceProvider<TypeDeclaration>(
    private val objectKey: ObjectKey,
    private val instance: TypeDeclaration
) : ObjectProvider<TypeDeclaration>(objectKey) {

    override val typeName: String = "instance"

    override fun provide(
        definitionContext: Context,
        dispatcher: DependencyDispatcher,
        searchSpec: SearchSpec
    ): TypeDeclaration {
        return instance
    }

}