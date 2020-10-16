package net.apptronic.core.android.viewmodel

import androidx.annotation.LayoutRes
import net.apptronic.core.base.SerialIdGenerator
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModel
import kotlin.reflect.KClass
import kotlin.reflect.full.superclasses

fun composeViewBinderFactory(vararg factories: ViewBinderFactory?): ViewBinderFactory {
    val list = factories.filterNotNull()
    return ChainViewBinderFactory(list)
}

fun viewBinderFactory(initializer: ConcreteViewBinderFactory.() -> Unit): ViewBinderFactory {
    return ConcreteViewBinderFactory().apply(initializer)
}

inline fun <reified ViewModelType : IViewModel> viewBinder(
    noinline builder: () -> ViewBinder<ViewModelType>
): ViewBinderFactory {
    return viewBinderFactory {
        add(builder)
    }
}

abstract class ViewBinderFactory {

    fun getBinder(typeId: Int): ViewBinder<*> {
        return lookupBinder(typeId) ?: GenericViewBinder()
    }

    fun getBinder(viewModel: IViewModel): ViewBinder<*> {
        return lookupBinder(viewModel) ?: GenericViewBinder()
    }

    fun getType(viewModel: IViewModel): Int {
        return lookupType(viewModel) ?: -1
    }

    internal abstract fun lookupBinder(typeId: Int): ViewBinder<*>?

    internal abstract fun lookupBinder(viewModel: IViewModel): ViewBinder<*>?

    internal abstract fun lookupType(viewModel: IViewModel): Int?

}

/**
 * This class is [ViewBinder] registry, which allows to build [ViewBinder] to corresponding
 * [ViewModel] when needed by adapters.
 */
class ConcreteViewBinderFactory internal constructor() : ViewBinderFactory() {

    companion object {
        val typeIdGenerator = SerialIdGenerator()
    }

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
    inline fun <reified ViewModelType : IViewModel> add(
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
    fun <ViewModelType : IViewModel> add(
        clazz: KClass<ViewModelType>,
        builder: () -> ViewBinder<ViewModelType>,
        @LayoutRes layoutResId: Int? = null
    ) {
        views[clazz] = ViewSpec(
            builder = builder,
            typeId = typeIdGenerator.nextId().toInt(),
            layoutResId = layoutResId
        )
    }

    override fun lookupBinder(typeId: Int): ViewBinder<*>? {
        return views.values.firstOrNull { it.typeId == typeId }?.builder?.invoke()
    }

    override fun lookupBinder(viewModel: IViewModel): ViewBinder<*>? {
        return searchRecursive(viewModel::class)?.build()
    }

    override fun lookupType(viewModel: IViewModel): Int? {
        return searchRecursive(viewModel::class)?.typeId
    }

    private fun searchRecursive(clazz: KClass<out IViewModel>): ViewSpec? {
        var iterableValue: KClass<*>? = clazz
        do {
            val result = views[iterableValue]
            if (result != null) {
                return result
            }
            iterableValue = iterableValue?.superclasses?.get(0)
        } while (iterableValue != null && iterableValue != IViewModel::class)
        return null
    }

    /**
     * Create new [ViewBinderFactory] which inherits all bindings from current but with possibility
     * to add new bindings which have no affecting for initial [ViewBinderFactory]
     */
    fun override(initializer: ViewBinderFactory.() -> Unit): ViewBinderFactory {
        return composeViewBinderFactory(viewBinderFactory(initializer), this)
    }

}

private class ChainViewBinderFactory(private val targets: List<ViewBinderFactory>) :
    ViewBinderFactory() {

    override fun lookupBinder(typeId: Int): ViewBinder<*>? {
        for (target in targets) {
            val binder = target.getBinder(typeId)
            if (binder != null) {
                return binder
            }
        }
        return null
    }

    override fun lookupBinder(viewModel: IViewModel): ViewBinder<*>? {
        for (target in targets) {
            val binder = target.getBinder(viewModel)
            if (binder != null) {
                return binder
            }
        }
        return null
    }

    override fun lookupType(viewModel: IViewModel): Int {
        for (target in targets) {
            val typeId = target.getType(viewModel)
            if (typeId > 0) {
                return typeId
            }
        }
        return -1
    }

}