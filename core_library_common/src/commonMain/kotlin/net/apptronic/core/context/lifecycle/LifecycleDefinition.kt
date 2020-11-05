package net.apptronic.core.context.lifecycle

val BASE_LIFECYCLE = defineLifecycle {
    // no additional stages
}

fun defineLifecycle(builder: LifecycleBuilder.() -> Unit): LifecycleDefinition {
    return LifecycleDefinitionImpl(builder)
}

interface LifecycleDefinition {

    fun createLifecycle(): Lifecycle

}

private class LifecycleDefinitionImpl(
        private val builder: LifecycleBuilder.() -> Unit
) : LifecycleDefinition {

    override fun createLifecycle(): Lifecycle {
        return LifecycleBuilderImpl().apply(builder).lifecycle
    }

}

interface LifecycleBuilder {

    fun addStage(definition: LifecycleStageDefinition)

}

private class LifecycleBuilderImpl : LifecycleBuilder {

    val lifecycle: Lifecycle = Lifecycle()

    override fun addStage(definition: LifecycleStageDefinition) {
        lifecycle.addStage(definition)
    }

}