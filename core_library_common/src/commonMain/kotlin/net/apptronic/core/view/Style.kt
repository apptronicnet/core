package net.apptronic.core.view

import net.apptronic.core.base.utils.isInstanceOf
import kotlin.reflect.KClass

class Style internal constructor(private val parent: Style?) {

    private val viewStyles = mutableListOf<ViewTypeStyle<*>>()

    private data class ViewTypeStyle<T : Any>(
            val type: KClass<T>,
            val styleFlow: T.() -> Unit
    ) {
        @Suppress("UNCHECKED_CAST")
        fun apply(target: Any) {
            if (target.isInstanceOf(type)) {
                styleFlow(target as T)
            }
        }
    }

    fun <T : Any> addTarget(type: KClass<T>, styleFlow: T.() -> Unit) {
        viewStyles.add(ViewTypeStyle(type, styleFlow))
    }

    inline fun <reified T : Any> addTarget(noinline styleFlow: T.() -> Unit) {
        addTarget(T::class, styleFlow)
    }

    inline fun <reified T : Any> addTarget(style: Style) {
        addTarget(T::class) {
            style.applyStyle(this)
        }
    }

    fun applyStyle(target: Any) {
        parent?.applyStyle(target)
        viewStyles.forEach {
            it.apply(target)
        }
    }

}

fun viewStyle(parent: Style? = null, builder: Style.() -> Unit): Style {
    return Style(parent).apply(builder)
}


inline fun <reified T : Any> viewTargetStyle(parent: Style? = null, noinline styleFlow: T.() -> Unit): Style {
    return viewStyle {
        addTarget(T::class, styleFlow)
    }
}