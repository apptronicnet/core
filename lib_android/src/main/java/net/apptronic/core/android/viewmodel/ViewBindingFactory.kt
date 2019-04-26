package net.apptronic.core.android.viewmodel

import androidx.annotation.LayoutRes
import net.apptronic.core.mvvm.viewmodel.ViewModel
import kotlin.reflect.KClass

fun viewBindingFactory(initializer: ViewBindingFactory.() -> Unit): ViewBindingFactory {
    return ViewBindingFactory().apply(initializer)
}

class ViewBindingFactory {

    private var indexGenerator = 1
    private val views = mutableMapOf<KClass<*>, ViewSpec>()

    private data class ViewSpec(
        val androidView: AndroidView<*>,
        val typeId: Int
    )

    inline fun <reified ViewModelType : ViewModel> addBinding(
        androidView: AndroidView<ViewModelType>,
        @LayoutRes layoutResId: Int? = null
    ) {
        addBinding(ViewModelType::class, androidView, layoutResId)
    }

    fun <ViewModelType : ViewModel> addBinding(
        clazz: KClass<ViewModelType>,
        androidView: AndroidView<ViewModelType>,
        @LayoutRes layoutResId: Int? = null
    ) {
        if (layoutResId != null) {
            androidView.layoutResId = layoutResId
        }
        views[clazz] = ViewSpec(androidView, indexGenerator++)
    }

    fun getAndroidView(typeId: Int): AndroidView<*> {
        return views.values.firstOrNull { it.typeId == typeId }?.androidView
            ?: throw IllegalArgumentException("Not registered")
    }

    fun getAndroidView(viewModel: ViewModel): AndroidView<*> {
        return views[viewModel::class]?.androidView
            ?: throw IllegalArgumentException("Not registered $viewModel")
    }

    fun getType(viewModel: ViewModel): Int {
        return views[viewModel::class]?.typeId ?: 0
    }

}