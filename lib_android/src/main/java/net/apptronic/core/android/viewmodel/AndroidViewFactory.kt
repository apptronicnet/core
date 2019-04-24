package net.apptronic.core.android.viewmodel

import net.apptronic.core.mvvm.viewmodel.ViewModel
import kotlin.reflect.KClass

class AndroidViewFactory {

    private val builderMap = mutableMapOf<KClass<*>, AndroidViewBuilder<*>>()

    private class AndroidViewBuilder<T : ViewModel>(val builder: (T) -> AndroidView<T>) {
        fun build(viewModel: ViewModel): AndroidView<T> {
            return builder.invoke(viewModel as T)
        }
    }

    inline fun <reified ViewModelType : ViewModel> addBinding(
        noinline builder: (ViewModelType) -> AndroidView<ViewModelType>
    ) {
        addBinding(ViewModelType::class, builder)
    }

    fun <ViewModelType : ViewModel> addBinding(
        clazz: KClass<ViewModelType>,
        builder: (ViewModelType) -> AndroidView<ViewModelType>
    ) {
        builderMap[clazz] = AndroidViewBuilder(builder)
    }

    fun <ViewModelType : ViewModel> build(viewModel: ViewModelType): AndroidView<ViewModelType>? {
        return builderMap[viewModel::class]?.build(viewModel) as AndroidView<ViewModelType>
    }

}