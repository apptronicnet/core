package net.apptronic.core.component.di.util

private interface TraceElement {

    fun append(builder: StringBuilder, indent: Int)

}

private fun StringBuilder.indent(count: Int) {
    append("| ")
    for (i in 0 until count) {
        append("    ") // 4 spaces
    }
}

private fun StringBuilder.appendElements(list: List<TraceElement>, indent: Int) {
    list.forEach {
        append("\n")
        it.append(this, indent)
    }
}

data class DependencyTrace(
        val elements: List<DependencyTraceElement>
) {

    fun print(target: (String) -> Unit = {
        println(it)
    }) {
        target.invoke(formatAsString())
    }

    fun formatAsString(): String {
        val sb = StringBuilder()
        sb.append("Dependency trace:")
        sb.appendElements(elements, 0)
        return sb.toString()
    }

}

data class DependencyTraceElement(
        val name: String,
        val instances: List<DependencyTraceInstance>,
        val modules: List<DependencyTraceModule>
) : TraceElement {

    override fun append(builder: StringBuilder, indent: Int) {
        builder.indent(indent)
        builder.append("Context: $name")
        if (instances.isNotEmpty()) {
            builder.indent(indent + 1)
            builder.append("Instances:")
            builder.appendElements(instances, indent + 1)
        }
        if (modules.isNotEmpty()) {
            builder.appendElements(modules, indent + 1)
        }
    }

}

data class DependencyTraceInstance(
        val key: String,
        val instance: Any
) : TraceElement {

    override fun append(builder: StringBuilder, indent: Int) {
        builder.indent(indent)
        builder.append("$key $instance")
    }

}

data class DependencyTraceModule(
        val name: String,
        val providers: List<DependencyTraceProvider>
) : TraceElement {

    override fun append(builder: StringBuilder, indent: Int) {
        builder.indent(indent)
        builder.append(if (name.isEmpty()) "[Module]" else name)
        if (providers.isNotEmpty()) {
            builder.appendElements(providers, indent + 1)
        }
    }

}


data class DependencyTraceProvider(
        val type: String,
        val keys: List<String>
) : TraceElement {

    override fun append(builder: StringBuilder, indent: Int) {
        builder.indent(indent)
        builder.append("$type: ")
        if (keys.size > 1) {
            builder.append("\n")
            builder.indent(indent + 1)
        }
        builder.append(keys.joinToString(separator = ", "))
    }

}