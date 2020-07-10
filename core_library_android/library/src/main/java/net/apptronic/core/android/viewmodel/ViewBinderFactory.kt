package net.apptronic.core.android.viewmodel

import androidx.annotation.LayoutRes
import net.apptronic.core.mvvm.viewmodel.ViewModel
import kotlin.reflect.KClass
import kotlin.reflect.full.superclasses

fun viewBinderFactory(initializer: ViewBinderFactory.() -> Unit): ViewBinderFactory {
    return ViewBinderFactory().apply(initializer)
}

inline fun <reified ViewModelType : ViewModel> viewBinder(
    noinline builder: () -> ViewBinder<ViewModelType>
): ViewBinderFactory {
    return viewBinderFactory {
        add(builder)
    }
}

/**
 * This class is [ViewBinder] registry, which allows to build [ViewBinder] to corresponding
 * [ViewModel] when needed by adapters.
 */
class ViewBinderFactory {

    private var parent: ViewBinderFactory? = null
    private var indexGenerator = 1
    private val views = mutableMapOf<KClass<*>, ViewSpec>()

    private data class ViewSpec(
        val builder: () -> ViewBinder<*>,
        val typeId: Int,
        val layoutResId: Int?
    ) {

        fun build(): ViewBinder<*> {
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
     * @param [builder] builder function of constructor reference for [ViewBinder]
     * @param [layoutResId] optional value for layout to be overridden instead of set in [ViewBinder]
     */
    inline fun <reified ViewModelType : ViewModel> add(
        noinline builder: () -> ViewBinder<ViewModelType>,
        @LayoutRes layoutResId: Int? = null
    ) {
        add(
            clazz = ViewModelType::class,
            builder = builder,
            layoutResId = layoutResId
        )
    }

    /**
     * Add binding
     * @param [clazz] type of [ViewModel] for bind
     * @param [builder] builder function of constructor reference for [ViewBinder]
     * @param [layoutResId] optional value for layout to be overridden instead of set in [ViewBinder]
     */
    fun <ViewModelType : ViewModel> add(
        clazz: KClass<ViewModelType>,
        builder: () -> ViewBinder<ViewModelType>,
        @LayoutRes layoutResId: Int? = null
    ) {
        views[clazz] = ViewSpec(
            builder = builder,
            typeId = indexGenerator++,
            layoutResId = layoutResId
        )
    }

    fun getBinder(typeId: Int): ViewBinder<*> {
        return views.values.firstOrNull { it.typeId == typeId }?.builder?.invoke()
            ?: throw IllegalArgumentException("ViewBinder is not registered for typeId=$typeId")
    }

    fun getBinder(viewModel: ViewModel): ViewBinder<*> {
        return searchRecursive(viewModel::class)?.build()
            ?: throw IllegalArgumentException("ViewBinder is not registered for $viewModel")
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
            ?: throw IllegalArgumentException("ViewBinder is not registered for $viewModel")
    }

    /**
     * Create new [ViewBinderFactory] which inherits all bindings from current but with possibility
     * to add new bindings which have no affecting for initial [ViewBinderFactory]
     */
    fun override(initializer: ViewBinderFactory.() -> Unit): ViewBinderFactory {
        return viewBinderFactory(initializer).also {
            it.parent = this
        }
    }

}