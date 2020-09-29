package net.apptronic.core.android.compat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.view.DialogDelegate
import net.apptronic.core.mvvm.viewmodel.IViewModel

abstract class ViewBinderDialogFragment<T : IViewModel> : CoreCompatDialogFragment<T>() {

    abstract fun buildViewBinder(): ViewBinder<T>

    private var viewBinder: ViewBinder<T>? = null

    final override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewBinder = buildViewBinder()
        val delegate = viewBinder!!.getViewDelegate<DialogDelegate<*>>()
        return delegate.performCreateDialogView(
            viewModel, viewBinder!!, container?.context ?: requireContext()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinder!!.performViewBinding(viewModel, view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinder = null
    }

}