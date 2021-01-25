package net.apptronic.core.context.di

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.apptronic.core.base.elapsedRealtimeMillis
import net.apptronic.core.context.Context
import net.apptronic.core.context.coroutines.contextCoroutineScope

internal fun <TypeDeclaration : Any> sharedProvider(
    objectKey: ObjectKey,
    builder: BuilderMethod<TypeDeclaration, SharedScope>,
    context: Context,
    fallbackCount: Int,
    fallbackLifetime: Long,
    managerDescriptor: DependencyDescriptor<out SharedScopeManager>?
): ObjectProvider<TypeDeclaration> {
    if (fallbackCount > 0 != fallbackLifetime > 0) {
        throw IllegalArgumentException("Both [fallbackCount] and [fallbackLifetime] should be set to be larger than 0 at same time for dependency $objectKey")
    }
    return SharedProvider(objectKey, builder, context, fallbackCount, fallbackLifetime, managerDescriptor)
}

interface SharedScopeOwner {

    fun resetAllFallbacks()

    fun resetFallback(parameters: Parameters)

}

private class SharedProvider<TypeDeclaration : Any>(
    objectKey: ObjectKey,
    builder: BuilderMethod<TypeDeclaration, SharedScope>,
    private val context: Context,
    private val fallbackCount: Int,
    private val fallbackLifetime: Long,
    private val managerDescriptor: DependencyDescriptor<out SharedScopeManager>?
) : ObjectBuilderProvider<TypeDeclaration, SharedScope>(objectKey, builder), SharedScopeOwner {

    override val typeName: String = "shared"

    private val coroutineScope = context.contextCoroutineScope

    override fun onModuleLoaded(definitionContext: Context) {
        super.onModuleLoaded(definitionContext)
        if (managerDescriptor != null) {
            val manager = definitionContext.dependencyProvider.inject(managerDescriptor)
            manager.onSharedScopeInitialized(this)
        }
    }

    private class SharedInstance<T>(
        val instance: T,
        val scope: SharedScope
    ) {
        var shareCount: Int = 0
        fun recycle() {
            scope.finalize()
        }
    }

    private class UnusedShare<T>(
        val parameters: Parameters,
        val share: SharedInstance<T>,
        val unusedFrom: Long
    ) {
        fun recycle() = share.recycle()
    }

    private val shares = mutableMapOf<Parameters, SharedInstance<TypeDeclaration>>()
    private val unused = mutableListOf<UnusedShare<TypeDeclaration>>()

    override fun provide(
        definitionContext: Context,
        dispatcher: DependencyDispatcher,
        searchSpec: SearchSpec
    ): TypeDeclaration {
        val providedContext = searchSpec.context
        val parameters = searchSpec.params
        val share = shares[parameters]
            ?: unused.firstOrNull {
                it.parameters == parameters
            }?.let {
                unused.remove(it)
                it.share
            } ?: createInstance(
                definitionContext, dispatcher, parameters
            )
        share.shareCount++
        providedContext.lifecycle.onExitFromActiveStage {
            share.shareCount--
            if (share.shareCount == 0) {
                shares.remove(parameters)
                unused(parameters, share)
            }
        }
        return share.instance
    }

    private fun createInstance(
        definitionContext: Context, dispatcher: DependencyDispatcher, parameters: Parameters
    ): SharedInstance<TypeDeclaration> {
        val scope = SharedScope(definitionContext, dispatcher, parameters)
        val instance = builder.invoke(scope)
        scope.autoRecycle(instance)
        val processed = definitionContext.plugins.nextProvide(definitionContext, instance)
        val share = SharedInstance(processed, scope)
        shares[parameters] = share
        return share
    }

    private fun unused(parameters: Parameters, share: SharedInstance<TypeDeclaration>) {
        if (fallbackCount <= 0 || fallbackLifetime <= 0) {
            share.recycle()
        } else {
            unused.add(UnusedShare(parameters, share, elapsedRealtimeMillis()))
            clearUnused()
        }
    }

    fun clearUnused() {
        val timestamp = elapsedRealtimeMillis()
        unused.removeAll {
            if (timestamp - it.unusedFrom > fallbackLifetime) {
                it.recycle()
                true
            } else false
        }
        while (unused.isNotEmpty() && unused.size > fallbackCount) {
            unused.removeAt(0).recycle()
        }
        scheduleAutoClearUnused()
    }

    private fun scheduleAutoClearUnused() {
        if (unused.isNotEmpty()) {
            val lifetimeInUnused = elapsedRealtimeMillis() - unused[0].unusedFrom
            val delayTime = fallbackLifetime - lifetimeInUnused
            if (delayTime > 0) {
                coroutineScope.launch {
                    delay(delayTime)
                    clearUnused()
                }
            }
        }
    }

    override fun resetAllFallbacks() {
        unused.forEach {
            it.recycle()
        }
        unused.clear()
    }

    override fun resetFallback(parameters: Parameters) {
        val toReset = unused.filter {
            it.parameters == parameters
        }
        unused.removeAll(toReset)
        toReset.forEach {
            it.recycle()
        }
    }

}
