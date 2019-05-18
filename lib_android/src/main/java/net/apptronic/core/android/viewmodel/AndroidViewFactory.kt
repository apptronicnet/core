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

class AndroidViewFactory {

    private var indexGenerator = 1
    private val views = mutableMapOf<KClass<*>, ViewSpec>()

    private data class ViewSpec(
        val builder: () -> AndroidView<*>,
        val typeId: Int,
        val layoutResId: Int?
    ) {

        fun build(): AndroidView<*> {
            return builder.invoke()
        }

    }

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
        return null
    }

    fun getType(viewModel: ViewModel): Int {
        return searchRecursive(viewModel::class)?.typeId
            ?: throw IllegalArgumentException("AndroidView is not registered for $viewModel")
    }

}