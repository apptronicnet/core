package net.apptronic.core.android.viewmodel.bindings.navigation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.apptronic.core.android.plugins.getViewBinderFactoryFromExtension
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderAdapter
import net.apptronic.core.android.viewmodel.view.DefaultViewContainerViewAdapter
import net.apptronic.core.android.viewmodel.view.ViewContainerViewAdapter
import net.apptronic.core.viewmodel.IViewModel

enum class BindingType(
    internal val detectAndCreate: Boolean,
    internal val createLayout:
    Boolean, internal val clearLayout: Boolean
) {
    /**
     * [ViewBinder] will create content view if target view is [ViewGroup] and have no children
     */
    AUTO(true, false, false),

    /**
     * Perform only binding of content view
     */
    BIND_ONLY(false, false, false),

    /**
     * [ViewBinder] will create content view before bind
     */
    CREATE_CONTENT(false, true, false),

    /**
     * [ViewBinder] will create content view before bind and destroy it on unbind
     */
    CREATE_CONTENT_AND_CLEAR(false, true, true)
}

fun BindingContainer.bindInnerViewModel(
    view: View,
    viewModel: IViewModel,
    viewBinder: ViewBinder<*>,
    bindingType: BindingType = BindingType.AUTO
) {
    +InnerViewBinding(view, viewModel, { viewBinder }, bindingType)
}

fun BindingContainer.bindInnerViewModel(
    view: View,
    viewModel: IViewModel,
    adapter: ViewBinderAdapter? = null,
    bindingType: BindingType = BindingType.AUTO
) {
    val resultFactory = adapter
        ?: viewModel.getViewBinderFactoryFromExtension()
        ?: throw IllegalArgumentException("BinderFactory should be provided by parameters or Context.installViewFactoryPlugin()")
    +InnerViewBinding(view, viewModel, resultFactory::getBinder, bindingType)
}

private class InnerViewBinding(
    private val targetView: View,
    private val targetViewModel: IViewModel,
    private val factory: (IViewModel) -> ViewBinder<*>,
    private val bindingType: BindingType
) : Binding() {

    private val layoutInflater = LayoutInflater.from(targetView.context)

    override fun onBind(viewModel: IViewModel, viewBinder: ViewBinder<*>) {
        val viewAdapter = viewBinder as? ViewContainerViewAdapter ?: DefaultViewContainerViewAdapter
        val targetBinder = factory.invoke(targetViewModel)
        val contentView: View = if (bindingType.detectAndCreate) {
            val container = targetView as? ViewGroup
            if (container != null && container.childCount == 0) {
                val contentView = viewAdapter.onCreateView(
                    viewModel,
                    targetBinder,
                    container.context,
                    layoutInflater,
                    container
                )
                viewAdapter.onAttachView(viewModel, contentView, container, container.childCount)
                contentView
            } else targetView
        } else if (bindingType.createLayout) {
            val container = targetView as ViewGroup
            container.removeAllViews()
            val contentView = viewAdapter.onCreateView(
                viewModel,
                targetBinder,
                container.context,
                layoutInflater,
                container
            )
            viewAdapter.onAttachView(viewModel, contentView, container, container.childCount)
            contentView
        } else targetView
        targetBinder.performViewBinding(targetViewModel, contentView)
    }

    override fun onUnbind(action: () -> Unit) {
        super.onUnbind(action)
        val container = targetView as? ViewGroup
        container?.removeAllViews()
    }

}

