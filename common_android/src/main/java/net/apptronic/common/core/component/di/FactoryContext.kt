package net.apptronic.common.core.component.di

class FactoryContext(
    val context: DIContext
) {

    inline fun <reified T> get(name: String = "") {
        return context.get(T::class, name)
    }

}