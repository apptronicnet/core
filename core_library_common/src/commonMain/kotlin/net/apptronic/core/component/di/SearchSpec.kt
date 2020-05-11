package net.apptronic.core.component.di

import net.apptronic.core.component.context.Context


internal class SearchSpec(
    /**
     * Context in which injection in requested
     */
    val context: Context,
    val key: ObjectKey,
    /**
     * Parameters, provided by injector
     */
    val params: Parameters
) {

    val lifecycleStage = context.lifecycle.getActiveStage()
            ?: throw IllegalStateException("Lifecycle was terminated")

    private val contextChain = mutableListOf<Context>()

    fun addContextToChain(context: Context) {
        contextChain.add(context)
    }

    fun getSearchPath(): String {
        return StringBuilder().apply {
            appendListFormatted("Parameters", "", params.getInstanceNames())
            contextChain.forEach { context ->
                append("${context::class.qualifiedName}\n")
                appendListFormatted("Instances", "    ", context.dependencyDispatcher().getInstanceNames())
                appendListFormatted("Modules", "    ", context.dependencyDispatcher().getModuleNames())
            }
        }.toString()
    }

    private fun StringBuilder.appendListFormatted(
        name: String,
        spacing: String,
        list: List<String>
    ) {
        if (list.isNotEmpty()) {
            append("$spacing$name:\n")
            list.forEach { itemName ->
                append("$spacing    $itemName\n")
            }
        }
    }

}