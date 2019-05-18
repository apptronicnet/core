package net.apptronic.core.threading

class WorkerDefinition internal constructor(private val name: String) {

    companion object {
        val DEFAULT = defineWorker("DEFAULT")
        val SYNCHRONOUS = defineWorker("SYNCHRONOUS")
        val UI = defineWorker("UI")
        val BACKGROUND_SINGLE_SHARED = defineWorker("BACKGROUND_SINGLE_SHARED")
        val BACKGROUND_SINGLE_INDIVIDUAL = defineWorker("BACKGROUND_SINGLE_INDIVIDUAL")
        val BACKGROUND_PARALLEL_SHARED = defineWorker("BACKGROUND_PARALLEL_SHARED")
        val BACKGROUND_PARALLEL_INDIVIDUAL = defineWorker("BACKGROUND_PARALLEL_INDIVIDUAL")
        val BACKGROUND_SERIAL = defineWorker("SERIAL_BACKGROUND")
    }

    override fun toString(): String {
        return "WorkerDefinition:$name"
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return this === other
    }

}

fun defineWorker(name: String): WorkerDefinition {
    return WorkerDefinition(name)
}