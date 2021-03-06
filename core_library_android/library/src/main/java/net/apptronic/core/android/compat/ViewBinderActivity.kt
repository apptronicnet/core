package net.apptronic.core.android.compat

import android.os.Bundle
import net.apptronic.core.android.plugins.getViewBinderAdapterFromExtension
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.view.ActivityViewProvider
import net.apptronic.core.android.viewmodel.view.DefaultActivityViewProvider
import net.apptronic.core.viewmodel.IViewModel

abstract class ViewBinderActivity<T : IViewModel> : CoreCompatActivity<T>() {

    open fun buildViewBinder(): ViewBinder<T> {
        return viewModel.getViewBinderAdapterFromExtension()!!.getBinder(viewModel)
    }

    private var viewBinder: ViewBinder<T>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinder = buildViewBinder()
        val viewProvider = viewBinder as? ActivityViewProvider ?: DefaultActivityViewProvider
        val contentView = viewProvider.onCreateActivityView(viewModel, viewBinder!!, this)
        setContentView(contentView)
        viewBinder!!.performViewBinding(viewModel, contentView)
    }

    override fun onDestroy() {
        viewBinder = null
        super.onDestroy()
    }

}