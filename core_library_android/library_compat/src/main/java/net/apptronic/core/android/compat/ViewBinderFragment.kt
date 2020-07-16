package net.apptronic.core.android.compat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.mvvm.viewmodel.ViewModel

abstract class ViewBinderFragment<T : ViewModel> : CoreCompatFragment<T>() {

    abstract fun buildViewBinder(): ViewBinder<T>

    private var viewBinder: ViewBinder<T>? = null

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinder = buildViewBinder()
        return if (container != null) {
            viewBinder!!.onCreateView(container)
        } else {
            viewBinder!!.onCreateView(requireContext())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinder!!.performViewBinding(view, viewModel)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinder = null
    }

}