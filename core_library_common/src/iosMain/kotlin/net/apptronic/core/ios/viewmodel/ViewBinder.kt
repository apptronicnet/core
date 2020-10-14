package net.apptronic.core.ios.viewmodel

import net.apptronic.core.component.plugin.extensionDescriptor
import net.apptronic.core.debugError
import net.apptronic.core.mvvm.viewmodel.IViewModel

private val ViewBinderExtensionsDescriptor = extensionDescriptor<ViewBinder<*, *>>()

abstract class ViewBinder<T : IViewModel, View : ViewHolder> : BindingContainer {

    private var viewModel: T? = null
    private var view: View? = null
    private var bindings: Bindings? = null

    @Suppress("UNCHECKED_CAST")
    fun performViewBinding(viewModel: IViewModel, view: ViewHolder) {
        if (viewModel.extensions[ViewBinderExtensionsDescriptor] != null) {
            debugError(Error("$viewModel already have bound view!!!"))
        }
        if (!viewModel.isStateBound()) {
            debugError(
                    Error(
                            "$viewModel in stage ${
                                viewModel.context.lifecycle.getActiveStage()
                                        ?.getStageName()
                            }"
                    )
            )
        }
        this.view = view as View
        this.viewModel = viewModel as T
        bindings = Bindings(viewModel, this)
        onBindView(view, viewModel)
        viewModel.extensions[ViewBinderExtensionsDescriptor] = this
        viewModel.doOnUnbind {
            getBindings().unbind()
            bindings = null
            viewModel.extensions.remove(ViewBinderExtensionsDescriptor)
        }
    }

    internal fun getBindings(): Bindings {
        return bindings ?: throw IllegalStateException("No bindings for $this")
    }

    fun getViewModel(): T {
        return viewModel ?: throw IllegalStateException("No viewModel bound for $this")
    }

    fun getView(): View {
        return view ?: throw IllegalStateException("No view bound for $this")
    }

    abstract fun onCreateView(): ViewHolder

    abstract fun onBindView(view: View, viewModel: T)

    final override fun onUnbind(action: () -> Unit) {
        getBindings().onUnbind(action)
    }

    final override fun add(binding: Binding) {
        getBindings().add(binding)
    }

}