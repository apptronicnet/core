package net.apptronic.core.android.viewmodel.bindings.navigation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.apptronic.core.android.plugins.getViewBinderAdapterFromExtension
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderAdapter
import net.apptronic.core.android.viewmodel.view.DefaultViewContainerViewAdapter
import net.apptronic.core.android.viewmodel.view.ViewContainerViewAdapter
import net.apptronic.core.viewmodel.IViewModel

fun BindingContainer.bindInnerViewModelToView(
    view: View,
    viewModel: IViewModel,
    viewBinder: ViewBinder<*>,
) {
    +InnerViewBinding(view.parent as ViewGroup, view, viewModel) { viewBinder }
}

fun BindingContainer.bindInnerViewModelToView(
    view: View,
    viewModel: IViewModel,
    adapter: ViewBinderAdapter? = null,
) {
    val resultAdapter = adapter
        ?: viewModel.getViewBinderAdapterFromExtension()
        ?: throw IllegalArgumentException("ViewBinderAdapter should be provided by parameters or Context.installViewBinderAdapterPlugin()")
    +InnerViewBinding(view.parent as ViewGroup, view, viewModel, resultAdapter::getBinder)
}

fun BindingContainer.bindInnerViewModelToContainer(
    container: ViewGroup,
    viewModel: IViewModel,
    viewBinder: ViewBinder<*>,
) {
    +InnerViewBinding(container, null, viewModel) { viewBinder }
}

fun BindingContainer.bindInnerViewModelToContainer(
    container: ViewGroup,
    viewModel: IViewModel,
    adapter: ViewBinderAdapter? = null,
) {
    val resultAdapter = adapter
        ?: viewModel.getViewBinderAdapterFromExtension()
        ?: throw IllegalArgumentException("ViewBinderAdapter should be provided by parameters or Context.installViewBinderAdapterPlugin()")
    +InnerViewBinding(container, null, viewModel, resultAdapter::getBinder)
}

private class InnerViewBinding(
    private val targetContainer: ViewGroup,
    private val targetView: View?,
    private val targetViewModel: IViewModel,
    private val builder: (IViewModel) -> ViewBinder<*>,
) : Binding() {

    private val layoutInflater = LayoutInflater.from(targetContainer.context)

    override fun onBind(viewModel: IViewModel, viewBinder: ViewBinder<*>) {
        val viewAdapter = viewBinder as? ViewContainerViewAdapter ?: DefaultViewContainerViewAdapter
        val targetBinder = builder.invoke(targetViewModel)
        val contentView = if (this.targetView != null) {
            this.targetView
        } else if (targetContainer.childCount > 0) {
            targetContainer.getChildAt(0)
        } else {
            val result = viewAdapter.onCreateView(
                viewModel,
                targetBinder,
                targetContainer.context,
                layoutInflater,
                targetContainer
            )
            viewAdapter.onAttachView(viewModel, result, targetContainer, 0)
            result
        }
        targetBinder.performViewBinding(targetViewModel, contentView)
    }

    override fun onUnbind(action: () -> Unit) {
        super.onUnbind(action)
        val container = targetView as? ViewGroup
        container?.removeAllViews()
    }

}

