package net.apptronic.core.context.lifecycle

import net.apptronic.core.context.Context

val DefaultLifecycleDefinition: LifecycleDefinition = LifecycleDefinitionImpl { }

fun defineLifecycle(builder: LifecycleBuilder.() -> Unit): LifecycleDefinition {
    return LifecycleDefinitionImpl(builder)
}

interface LifecycleDefinition {

    fun assignTo(context: Context)

}

private class LifecycleDefinitionImpl(
    private val builder: LifecycleBuilder.() -> Unit
) : LifecycleDefinition {

    override fun assignTo(context: Context) {
        when (context.lifecycle.lifecycleDefinition) {
            null -> {
                context.lifecycle.lifecycleDefinition = this
                LifecycleBuilderImpl(context.lifecycle).apply(builder)
            }
            this -> {
                // ignore as already assigned
            }
            else -> throw LifecycleDefinitionAssignmentFailedException()
        }
    }

}

interface LifecycleBuilder {

    fun addStage(definition: LifecycleStageDefinition)

}

private class LifecycleBuilderImpl(private val lifecycle: Lifecycle) : LifecycleBuilder {

    override fun addStage(definition: LifecycleStageDefinition) {
        lifecycle.addStage(definition)
    }

}