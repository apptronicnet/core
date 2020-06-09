package net.apptronic.core.android.viewmodel.bindings.navigation

import android.view.View
import android.view.ViewGroup
import net.apptronic.core.android.plugins.getAndroidViewFactoryFromExtension
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.AndroidViewFactory
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.mvvm.viewmodel.ViewModel

enum class BindingType(
    internal val detectAndCreate: Boolean,
    internal val createLayout:
    Boolean, internal val clearLayout: Boolean
) {
    /**
     * [AndroidView] will create content view if target view is [ViewGroup] and have no children
     */
    AUTO(true, false, false),

    /**
     * Perform only binding of content view
     */
    BIND_ONLY(false, false, false),

    /**
     * [AndroidView] will create content view before bind
     */
    CREATE_CONTENT(false, true, false),

    /**
     * [AndroidView] will create content view before bind and destroy it on unbind
     */
    CREATE_CONTENT_AND_CLEAR(false, true, true)
}

fun BindingContainer.bindInnerViewModel(
    view: View,
    viewModel: ViewModel,
    androidView: AndroidView<*>,
    bindingType: BindingType = BindingType.AUTO
) {
    add(AndroidViewBinding(view, viewModel, { androidView }, bindingType))
}

fun BindingContainer.bindInnerViewModel(
    view: View,
    viewModel: ViewModel,
    factory: AndroidViewFactory? = null,
    bindingType: BindingType = BindingType.AUTO
) {
    val resultFactory = factory
        ?: viewModel.getAndroidViewFactoryFromExtension()
        ?: throw IllegalArgumentException("AndroidViewFactory should be provided by parameters or Context.installViewFactoryPlugin()")
    add(AndroidViewBinding(view, viewModel, resultFactory::getAndroidView, bindingType))
}

private class AndroidViewBinding(
    private val targetView: View,
    private val targetViewModel: ViewModel,
    private val factory: (ViewModel) -> AndroidView<*>,
    private val bindingType: BindingType
) : Binding() {

    override fun onBind(viewModel: ViewModel, androidView: AndroidView<*>) {
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

