package net.apptronic.core.android.compat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.apptronic.core.android.plugins.getViewBinderAdapterFromExtension
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.view.DefaultViewContainerViewAdapter
import net.apptronic.core.android.viewmodel.view.ViewContainerViewAdapter
import net.apptronic.core.viewmodel.IViewModel

abstract class ViewBinderFragment<T : IViewModel> : CoreCompatFragment<T>() {

    open fun buildViewBinder(): ViewBinder<T> {
        return viewModel.getViewBinderAdapterFromExtension()!!.getBinder(viewModel)
    }

    private var viewBinder: ViewBinder<T>? = null

    final override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewBinder = buildViewBinder()
        val viewAdapter = viewBinder as? ViewContainerViewAdapter ?: DefaultViewContainerViewAdapter
        return viewAdapter.onCreateView(
            viewModel, viewBinder!!, container?.context ?: requireContext(), inflater, container
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