package net.apptronic.core.component.lifecycle

fun lifecycleStage(name: String): LifecycleStageDefinition {
    return LifecycleStageDefinition(name)
}

class LifecycleStageDefinition internal constructor(val name: String) {

    override fun equals(other: Any?): Boolean {
        return this === other
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun toString(): String {
        return "LifecycleStageDefinition[$name]/${hashCode()}"
    }

}