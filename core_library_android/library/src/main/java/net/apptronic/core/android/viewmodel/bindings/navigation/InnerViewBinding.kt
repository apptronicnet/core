package net.apptronic.core.android.viewmodel.bindings.navigation

import android.view.View
import android.view.ViewGroup
import net.apptronic.core.android.plugins.getViewBinderFactoryFromExtension
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.mvvm.viewmodel.ViewModel

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
    viewModel: ViewModel,
    viewBinder: ViewBinder<*>,
    bindingType: BindingType = BindingType.AUTO
) {
    +InnerViewBinding(view, viewModel, { viewBinder }, bindingType)
}

fun BindingContainer.bindInnerViewModel(
    view: View,
    viewModel: ViewModel,
    factory: ViewBinderFactory? = null,
    bindingType: BindingType = BindingType.AUTO
) {
    val resultFactory = factory
        ?: viewModel.getViewBinderFactoryFromExtension()
        ?: throw IllegalArgumentException("AndroidViewFactory should be provided by parameters or Context.installViewFactoryPlugin()")
    +InnerViewBinding(view, viewModel, resultFactory::getBinder, bindingType)
}

private class InnerViewBinding(
    private val targetView: View,
    private val targetViewModel: ViewModel,
    private val factory: (ViewModel) -> ViewBinder<*>,
    private val bindingType: BindingType
) : Binding() {

    override fun onBind(viewModel: ViewModel, viewBinder: ViewBinder<*>) {
        val targetAndroidView = factory.invoke(targetViewModel)
        val contentView: View = if (bindingType.detectAndCreate) {
            val container = targetView as? ViewGroup
            if (container != null && container.childCount == 0) {
                val contentView = targetAndroidView.onCreateView(container)
                targetAndroidView.onAttachView(contentView, container)
                contentView
            } else targetView
        } else if (bindingType.createLayout) {
            val container = targetView as ViewGroup
            container.removeAllViews()
            val contentView = targetAndroidView.onCreateView(container)
            targetAndroidView.onAttachView(contentView, container)
            contentView
        } else targetView
        targetAndroidView.bindView(contentView, targetViewModel)
    }

    override fun onUnbind(action: () -> Unit) {
        super.onUnbind(action)
        val container = targetView as? ViewGroup
        container?.removeAllViews()
    }

}

