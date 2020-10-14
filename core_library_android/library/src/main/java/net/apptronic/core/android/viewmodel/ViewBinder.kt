package net.apptronic.core.android.viewmodel

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Parcelable
import android.util.SparseArray
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import net.apptronic.core.android.viewmodel.view.ActivityDelegate
import net.apptronic.core.android.viewmodel.view.DialogDelegate
import net.apptronic.core.android.viewmodel.view.ViewContainerDelegate
import net.apptronic.core.component.plugin.extensionDescriptor
import net.apptronic.core.debugError
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelLifecycle
import net.apptronic.core.mvvm.viewmodel.navigation.ViewModelItem
import kotlin.reflect.KClass

private val SavedInstanceStateExtensionDescriptor = extensionDescriptor<SparseArray<Parcelable>>()
private val ViewBinderExtensionsDescriptor = extensionDescriptor<ViewBinder<*>>()

fun IViewModel.requireBoundView() {
//    doOnVisible {
//        if (extensions[ViewBinderExtensionsDescriptor] == null) {
//            debugError(Error("$this have no ViewBinder"))
//        }
//    }
}

/**
 * Responsible for creating [View] and binding it to [ViewModel]. Generally implements "View" layer.
 *
 * There are several methods for usage with [Activity], [Dialog] or generic usage as [View] under
 * some [ViewGroup] container.
 */
abstract class ViewBinder<T : IViewModel> : BindingContainer {

    /**
     * Specify layout resource to be used for creation or [View] using default implementation.
     * If null - should override each method [onCreateView] to be used for corresponding binding
     * or navigator.
     */
    @LayoutRes
    open var layoutResId: Int? = null

    private var item: ViewModelItem? = null
    private var viewModel: T? = null
    private var view: View? = null
    private var bindings: Bindings? = null

    private val delegates = mutableMapOf<KClass<*>, Any>()

    open fun onCreateView(
        context: Context, inflater: LayoutInflater, container: ViewGroup?
    ): View {
        val layoutResId = this.layoutResId
        return if (layoutResId != null) {
            inflater.inflate(layoutResId, container, false)
        } else {
            createFallbackView(context, this::class.simpleName ?: "ViewBinder")
        }
    }

    fun createFallbackView(context: Context, fallbackText: String): View {
        return TextView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
            setTextColor(Color.BLACK)
            setBackgroundColor(Color.LTGRAY)
            text = fallbackText
        }
    }

    inline fun <reified Delegate : Any> getViewDelegate(): Delegate {
        return getViewDelegate(Delegate::class)
    }

    /**
     * Called to create delegate which is responsible for interaction with concrete view type
     */
    @Suppress("UNCHECKED_CAST")
    fun <Delegate : Any> getViewDelegate(type: KClass<Delegate>): Delegate {
        delegates[type]?.let {
            return@getViewDelegate it as Delegate
        }
        val delegate = onCreateViewDelegate(type) as? Delegate
            ?: throw UnsupportedViewDelegateException("$this does not provide delegate for type $type")
        delegates[type] = delegate
        return delegates[type] as Delegate
    }

    open fun <Delegate : Any> onCreateViewDelegate(type: KClass<Delegate>): Any? {
        return when (type) {
            ViewContainerDelegate::class -> ViewContainerDelegate<T>()
            ActivityDelegate::class -> ActivityDelegate<T>()
            DialogDelegate::class -> DialogDelegate<T>()
            else -> null
        }
    }

    fun getItem(): ViewModelItem {
        return item ?: throw IllegalStateException("No ViewModelItem for $this")
    }

    fun getViewModel(): T {
        return viewModel ?: throw IllegalStateException("No viewModel bound for $this")
    }

    fun getView(): View {
        return view ?: throw IllegalStateException("No view bound for $this")
    }

    internal fun getBindings(): Bindings {
        return bindings ?: throw IllegalStateException("No bindings for $this")
    }

    private fun preCheck(viewModel: IViewModel) {
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

    }

    fun performViewBinding(viewModel: IViewModel, view: View) {
        preCheck(viewModel)
        performViewBindingInternal(viewModel, view)
    }

    /**
     * Bind [view] to [viewModel]
     */
    @Suppress("UNCHECKED_CAST")
    fun performViewBinding(item: ViewModelItem, view: View) {
        preCheck(item.viewModel)
        this.item = item
        performViewBindingInternal(item.viewModel, view)

    }

    private fun performViewBindingInternal(viewModel: IViewModel, view: View) {
        this.view = view
        this.viewModel = viewModel as T
        bindings = Bindings(viewModel, this)
        onBindView(view, viewModel)

        viewModel.extensions[SavedInstanceStateExtensionDescriptor]?.let {
            view.restoreHierarchyState(it)
        }
        viewModel.extensions[ViewBinderExtensionsDescriptor] = this
        viewModel.doOnUnbind {
            val hierarchyState = SparseArray<Parcelable>()
            view.saveHierarchyState(hierarchyState)
            viewModel.extensions[SavedInstanceStateExtensionDescriptor] = hierarchyState
            getBindings().unbind()
            bindings = null
            viewModel.extensions.remove(ViewBinderExtensionsDescriptor)
        }
    }

    /**
     * Called when [view] is binding to the [viewModel]. At this time [viewModel] lifecycle
     * is in stage [ViewModelLifecycle.STAGE_BOUND]
     */
    protected abstract fun onBindView(view: View, viewModel: T)

    final override fun onUnbind(action: () -> Unit) {
        getBindings().onUnbind(action)
    }

    final override fun add(binding: Binding) {
        getBindings().add(binding)
    }

}

