package net.apptronic.core.threading

class WorkerDefinition internal constructor(private val name: String) {

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