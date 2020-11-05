package net.apptronic.core.android.compat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.apptronic.core.android.plugins.getViewBinderFactoryFromExtension
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.view.DefaultDialogViewAdapter
import net.apptronic.core.android.viewmodel.view.DialogViewAdapter
import net.apptronic.core.viewmodel.IViewModel

abstract class ViewBinderDialogFragment<T : IViewModel> : CoreCompatDialogFragment<T>() {

    open fun buildViewBinder(): ViewBinder<T> {
        return viewModel.getViewBinderFactoryFromExtension()!!.getBinder(viewModel)
    }

    private var viewBinder: ViewBinder<T>? = null

    final override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewBinder = buildViewBinder()
        val viewAdapter = viewBinder as? DialogViewAdapter ?: DefaultDialogViewAdapter
        return viewAdapter.onCreateDialogView(
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