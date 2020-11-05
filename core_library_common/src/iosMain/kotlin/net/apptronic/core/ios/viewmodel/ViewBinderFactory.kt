package net.apptronic.core.ios.viewmodel

import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.ViewModel
import kotlin.reflect.KClass

fun viewBinderFactory(initializer: ViewBinderFactory.() -> Unit): ViewBinderFactory {
    return ViewBinderFactory().apply(initializer)
}

inline fun <reified ViewModelType : IViewModel> viewBinder(
        noinline builder: () -> ViewBinder<ViewModelType, *>
): ViewBinderFactory {
    return viewBinderFactory {
        add(builder)
    }
}

class ViewBinderFactory {

    private var parent: ViewBinderFactory? = null
    private var indexGenerator = 1
    private val views = mutableMapOf<KClass<*>, ViewSpec>()

    private data class ViewSpec(
            val builder: () -> ViewBinder<*, *>,
            val typeId: Int
    ) {

        fun build(): ViewBinder<*, *> {
            return builder.invoke()
        }

    }

    /**
     * Add binding
     * @param [ViewModelType] type of [ViewModel] for bind
     * @param [builder] builder function of constructor reference for [ViewBinder]
     * @param [layoutResId] optional value for layout to be overridden instead of set in [ViewBinder]
     */
    inline fun <reified ViewModelType : IViewModel> add(
            noinline builder: () -> ViewBinder<ViewModelType, *>) {
        add(
                clazz = ViewModelType::class,
                builder = builder
        )
    }

    /**
     * Add binding
     * @param [clazz] type of [ViewModel] for bind
     * @param [builder] builder function of constructor reference for [ViewBinder]
     * @param [layoutResId] optional value for layout to be overridden instead of set in [ViewBinder]
     */
    fun <ViewModelType : IViewModel> add(
            clazz: KClass<ViewModelType>,
            builder: () -> ViewBinder<ViewModelType, *>,
    ) {
        views[clazz] = ViewSpec(
                builder = builder,
                typeId = indexGenerator++
        )
    }

    fun getBinder(typeId: Int): ViewBinder<*, *> {
        return views.values.firstOrNull { it.typeId == typeId }?.builder?.invoke()
                ?: GenericViewBinder()
    }

    fun getBinder(viewModel: IViewModel): ViewBinder<*, *> {
        return searchRecursive(viewModel::class)?.build() ?: GenericViewBinder()
    }

    private fun searchRecursive(clazz: KClass<out IViewModel>): ViewSpec? {
        val result = views[clazz]
        if (result != null) {
            return result
        }
        val parent = this.parent
        if (parent != null) {
            return parent.searchRecursive(clazz)
        }
        return null
    }

    fun getType(viewModel: IViewModel): Int {
        return searchRecursive(viewModel::class)?.typeId ?: 0
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