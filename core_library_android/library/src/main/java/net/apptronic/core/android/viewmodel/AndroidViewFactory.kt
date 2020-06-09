package net.apptronic.core.android.viewmodel

import androidx.annotation.LayoutRes
import net.apptronic.core.mvvm.viewmodel.ViewModel
import kotlin.reflect.KClass
import kotlin.reflect.full.superclasses

fun androidViewFactory(initializer: AndroidViewFactory.() -> Unit): AndroidViewFactory {
    return AndroidViewFactory().apply(initializer)
}

inline fun <reified ViewModelType : ViewModel> androidView(
    noinline builder: () -> AndroidView<ViewModelType>
): AndroidViewFactory {
    return androidViewFactory {
        addBinding(builder)
    }
}

/**
 * This class is [AndroidView] registry, which allows to build [AndroidView] to corresponding
 * [ViewModel] when needed by adapters.
 */
class AndroidViewFactory {

    private var parent: AndroidViewFactory? = null
    private var indexGenerator = 1
    private val views = mutableMapOf<KClass<*>, ViewSpec>()

    private data class ViewSpec(
        val builder: () -> AndroidView<*>,
        val typeId: Int,
        val layoutResId: Int?
    ) {

        fun build(): AndroidView<*> {
            return builder.invoke().also {
                if (layoutResId != null) {
                    it.layoutResId = layoutResId
                }
            }
        }

    }

    /**
     * Add binding
     * @param [ViewModelType] type of [ViewModel] for bind
     * @param [builder] builder function of constructor reference for [AndroidView]
     * @param [layoutResId] optional value for layout to be overridden instead of set in [AndroidView]
     */
    inline fun <reified ViewModelType : ViewModel> addBinding(
        noinline builder: () -> AndroidView<ViewModelType>,
        @LayoutRes layoutResId: Int? = null
    ) {
        addBinding(
            clazz = ViewModelType::class,
            builder = builder,
            layoutResId = layoutResId
        )
    }

    /**
     * Add binding
     * @param [clazz] type of [ViewModel] for bind
     * @param [builder] builder function of constructor reference for [AndroidView]
     * @param [layoutResId] optional value for layout to be overridden instead of set in [AndroidView]
     */
    fun <ViewModelType : ViewModel> addBinding(
        clazz: KClass<ViewModelType>,
        builder: () -> AndroidView<ViewModelType>,
        @LayoutRes layoutResId: Int? = null
    ) {
        views[clazz] = ViewSpec(
            builder = builder,
            typeId = indexGenerator++,
            layoutResId = layoutResId
        )
    }

    fun getAndroidView(typeId: Int): AndroidView<*> {
        return views.values.firstOrNull { it.typeId == typeId }?.builder?.invoke()
            ?: throw IllegalArgumentException("AndroidView is not registered for typeId=$typeId")
    }

    fun getAndroidView(viewModel: ViewModel): AndroidView<*> {
        return searchRecursive(viewModel::class)?.build()
            ?: throw IllegalArgumentException("AndroidView is not registered for $viewModel")
    }

    private fun searchRecursive(clazz: KClass<out ViewModel>): ViewSpec? {
        var iterableValue: KClass<*>? = clazz
        do {
            val result = views[iterableValue]
            if (result != null) {
                return result
            }
            iterableValue = iterableValue?.superclasses?.get(0)
        } while (iterableValue != null && iterableValue != ViewModel::class)
        val parent = this.parent
        if (parent != null) {
            return parent.searchRecursive(clazz)
        }
        return null
    }

    fun getType(viewModel: ViewModel): Int {
        return searchRecursive(viewModel::class)?.typeId
            ?: throw IllegalArgumentException("AndroidView is not registered for $viewModel")
    }

    /**
     * Create new [AndroidViewFactory] which inherits all bindings from current but with possibility
     * to add new bindings which have no affecting for initial [AndroidViewFactory]
     */
    fun override(initializer: AndroidViewFactory.() -> Unit): AndroidViewFactory {
        return androidViewFactory(initializer).also {
            it.parent = this
        }
    }

}