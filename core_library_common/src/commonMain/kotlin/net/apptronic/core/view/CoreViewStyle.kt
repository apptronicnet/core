package net.apptronic.core.view

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.base.utils.isInstanceOf
import kotlin.reflect.KClass

/**
 * Class that defines style which is applied dynamically to any view
 */
@UnderDevelopment
class CoreViewStyle internal constructor() {

    private val includes = mutableListOf<CoreViewStyle>()
    private val viewStyles = mutableListOf<ViewTypeStyle<*>>()

    private data class ViewTypeStyle<T : ICoreView>(
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

    fun common(styleFlow: ICoreView.() -> Unit) {
        viewStyles.add(ViewTypeStyle(ICoreView::class, styleFlow))
    }

    fun <T : ICoreView> typed(type: KClass<T>, styleFlow: T.() -> Unit) {
        viewStyles.add(ViewTypeStyle(type, styleFlow))
    }

    inline fun <reified T : ICoreView> typed(noinline styleFlow: T.() -> Unit) {
        typed(T::class, styleFlow)
    }

    inline fun <reified T : ICoreView> typed(style: CoreViewStyle) {
        typed(T::class) {
            style.applyTo(this)
        }
    }

    fun include(style: CoreViewStyle) {
        includes.add(style)
    }

    fun applyTo(target: Any) {
        includes.forEach {
            it.applyTo(target)
        }
        viewStyles.forEach {
            it.apply(target)
        }
    }

}

fun viewTheme(builder: CoreViewStyle.() -> Unit): CoreViewStyle {
    return CoreViewStyle().apply(builder)
}


inline fun <reified T : ICoreView> viewStyle(parent: CoreViewStyle? = null, noinline styleFlow: T.() -> Unit): CoreViewStyle {
    return viewTheme {
        typed(T::class, styleFlow)
    }
}