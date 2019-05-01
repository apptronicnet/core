package net.apptronic.core.android.viewmodel

import androidx.annotation.LayoutRes
import net.apptronic.core.mvvm.viewmodel.ViewModel
import kotlin.reflect.KClass

fun androidViewFactory(initializer: AndroidViewFactory.() -> Unit): AndroidViewFactory {
    return AndroidViewFactory().apply(initializer)
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
            ?: throw IllegalArgumentException("AndroidView is registered for typeId=$typeId")
    }

    fun getAndroidView(viewModel: ViewModel): AndroidView<*> {
        return views[viewModel::class]?.builder?.invoke()
            ?: throw IllegalArgumentException("AndroidView is not registered for $viewModel")
    }

    fun getType(viewModel: ViewModel): Int {
        return views[viewModel::class]?.typeId ?: 0
    }

}