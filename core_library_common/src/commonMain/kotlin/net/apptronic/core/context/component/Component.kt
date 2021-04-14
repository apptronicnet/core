package net.apptronic.core.context.component

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.di.DependencyDescriptor
import net.apptronic.core.context.di.DependencyProvider
import net.apptronic.core.context.di.Parameters
import net.apptronic.core.context.di.emptyParameters
import net.apptronic.core.context.lifecycle.Lifecycle
import net.apptronic.core.context.lifecycle.LifecycleDefinition
import net.apptronic.core.context.lifecycle.LifecycleStage
import net.apptronic.core.context.lifecycle.LifecycleStageDefinition
import net.apptronic.core.context.plugin.Extendable
import net.apptronic.core.context.plugin.Extensions

interface IComponent : Extendable, Contextual {

    val componentId: Long

    fun onceStage(definition: LifecycleStageDefinition, key: String, action: () -> Unit)

    fun onEnterStage(definition: LifecycleStageDefinition, callback: LifecycleStage.OnEnterHandler.() -> Unit)

    fun onExitStage(definition: LifecycleStageDefinition, callback: LifecycleStage.OnExitHandler.() -> Unit)

    fun doOnTerminate(callback: LifecycleStage.OnExitHandler.() -> Unit)

}

open class Component(
    final override val context: Context,
    lifecycleDefinition: LifecycleDefinition? = null
) : IComponent {

    init {
        lifecycleDefinition?.assignTo(context)
    }

    final override val componentId: Long = ComponentRegistry.nextId()

    final override val extensions: Extensions = Extensions()

    final override val dependencyProvider: DependencyProvider
        get() = context.dependencyDispatcher

    final override fun onceStage(definition: LifecycleStageDefinition, key: String, action: () -> Unit) {
        context.lifecycle[definition]?.doOnce(key, action)
    }

    final override fun onEnterStage(
        definition: LifecycleStageDefinition,
        callback: LifecycleStage.OnEnterHandler.() -> Unit
    ) {
        context.lifecycle[definition]?.doOnEnter(callback)
    }

    final override fun onExitStage(
        definition: LifecycleStageDefinition,
        callback: LifecycleStage.OnExitHandler.() -> Unit
    ) {
        context.lifecycle[definition]?.doOnExit(callback)
    }

    final override fun doOnTerminate(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        onExitStage(Lifecycle.ROOT_STAGE, callback)
    }

    inline fun <reified TypeDeclaration : Any> inject(
        params: Parameters = emptyParameters()
    ): TypeDeclaration {
        return context.dependencyDispatcher.inject(TypeDeclaration::class, params)
    }

    inline fun <reified TypeDeclaration : Any> optional(
        params: Parameters = emptyParameters()
    ): TypeDeclaration? {
        return context.dependencyDispatcher.optional(TypeDeclaration::class, params)
    }

    inline fun <reified TypeDeclaration : Any> injectLazy(
        params: Parameters = emptyParameters()
    ): Lazy<TypeDeclaration> {
        return context.dependencyDispatcher.injectLazy(TypeDeclaration::class, params)
    }

    inline fun <reified TypeDeclaration : Any> optionalLazy(
        params: Parameters = emptyParameters()
    ): Lazy<TypeDeclaration?> {
        return context.dependencyDispatcher.optionalLazy(TypeDeclaration::class, params)
    }

    fun <TypeDeclaration> inject(
        descriptor: DependencyDescriptor<TypeDeclaration>,
        params: Parameters = emptyParameters()
    ): TypeDeclaration {
        return context.dependencyDispatcher.inject(descriptor, params)
    }

    fun <TypeDeclaration> optional(
        descriptor: DependencyDescriptor<TypeDeclaration>,
        params: Parameters = emptyParameters()
    ): TypeDeclaration? {
        return context.dependencyDispatcher.optional(descriptor, params)
    }

    fun <TypeDeclaration> injectLazy(
        descriptor: DependencyDescriptor<TypeDeclaration>,
        params: Parameters = emptyParameters()
    ): Lazy<TypeDeclaration> {
        return context.dependencyDispatcher.injectLazy(descriptor, params)
    }

    fun <TypeDeclaration> optionalLazy(
        descriptor: DependencyDescriptor<TypeDeclaration>,
        params: Parameters = emptyParameters()
    ): Lazy<TypeDeclaration?> {
        return context.dependencyDispatcher.optionalLazy(descriptor, params)
    }

}
