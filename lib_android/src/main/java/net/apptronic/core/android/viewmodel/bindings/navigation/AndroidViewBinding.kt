package net.apptronic.core.android.viewmodel.bindings.navigation

import android.view.View
import android.view.ViewGroup
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.AndroidViewFactory
import net.apptronic.core.android.viewmodel.Binding
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

fun androidViewBinding(
    view: View,
    viewModel: ViewModel,
    androidView: AndroidView<*>,
    bindingType: BindingType = BindingType.AUTO
): AndroidViewBinding {
    return AndroidViewBinding(view, viewModel, { androidView }, bindingType)
}

fun androidViewBinding(
    view: View,
    viewModel: ViewModel,
    factory: AndroidViewFactory,
    bindingType: BindingType = BindingType.AUTO
): AndroidViewBinding {
    return AndroidViewBinding(view, viewModel, factory::getAndroidView, bindingType)
}

class AndroidViewBinding(
    private val view: View,
    private val targetViewModel: ViewModel,
    private val factory: (ViewModel) -> AndroidView<*>,
    private val bindingType: BindingType
) : Binding() {

    override fun onBind(viewModel: ViewModel, androidView: AndroidView<*>) {
        val targetAndroidView = factory.invoke(targetViewModel)
        if (bindingType.detectAndCreate) {
            val container = view as? ViewGroup
            if (container != null && container.childCount == 0) {
                targetAndroidView.onAttachView(container)
            }
        }
        if (bindingType.createLayout) {
            val container = view as ViewGroup
            container.removeAllViews()
            targetAndroidView.onAttachView(container)
        }
        targetAndroidView.bindView(view, targetViewModel)
        onUnbind {
            if (bindingType.clearLayout) {
                val container = view as ViewGroup
                container.removeAllViews()
            }
        }
    }

}

