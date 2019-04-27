package net.apptronic.core.android.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import net.apptronic.core.mvvm.viewmodel.ViewModel

abstract class AndroidView<T : ViewModel> {

    @LayoutRes
    var layoutResId: Int? = null

    private var viewModel: ViewModel? = null
    private var view: View? = null

    private val onUnbindActions = mutableListOf<() -> Unit>()
    private val bindingList = mutableListOf<Binding>()

    open fun onCreateView(container: ViewGroup): View {
        return layoutResId?.let {
            LayoutInflater.from(container.context).inflate(it, container, false)
        } ?: throw IllegalStateException("[layoutResId] is not specified for $this")
    }

    open fun onAttachView(container: ViewGroup) {
        container.addView(onCreateView(container))
    }

    internal fun getViewModel(): ViewModel {
        return viewModel ?: throw IllegalStateException("No viewModel bound for $this")
    }

    internal fun getView(): View {
        return view ?: throw IllegalStateException("No view bound for $this")
    }

    internal fun bindView(view: View, viewModel: ViewModel) {
        this.view = view
        this.viewModel = viewModel
        onBindView(view, viewModel as T)
        viewModel.getLifecycle().onExitFromActiveStage {
            onUnbindActions.forEach { it.invoke() }
            onUnbindActions.clear()
            bindingList.forEach {
                it.unbind()
            }
            bindingList.clear()
        }
    }

    abstract fun onBindView(view: View, viewModel: T)

    infix fun AndroidView<*>.add(binding: Binding) {
        bindingList.add(binding)
        binding.onBind()
    }

    operator fun Binding.unaryPlus() {
        bindingList.add(this)
        this.onBind()
    }

    enum class BindingType(internal val createLayout: Boolean, internal val clearLayout: Boolean) {
        BIND_ONLY(false, false),
        CREATE_CONTENT(true, false),
        CREATE_CONTENT_AND_CLEAR(true, true)
    }

    class AndroidViewBinding(
        private val view: View,
        private val viewModel: ViewModel,
        private val androidView: AndroidView<*>,
        private val bindingType: BindingType = BindingType.BIND_ONLY
    ) : Binding() {

        override fun onBind() {
            if (bindingType.createLayout) {
                val container = view as ViewGroup
                container.removeAllViews()
                androidView.onAttachView(container)
            }
            androidView.bindView(view, viewModel)
            onUnbind {
                if (bindingType.clearLayout) {
                    val container = view as ViewGroup
                    container.removeAllViews()
                }
            }
        }

    }

}

