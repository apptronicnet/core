package net.apptronic.core.android.compat

import android.os.Bundle
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.view.ActivityDelegate
import net.apptronic.core.mvvm.viewmodel.IViewModel

abstract class ViewBinderActivity<T : IViewModel> : CoreCompatActivity<T>() {

    abstract fun buildViewBinder(): ViewBinder<T>

    private var viewBinder: ViewBinder<T>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinder = buildViewBinder()
        val delegate = viewBinder!!.getViewDelegate<ActivityDelegate<*>>()
        val contentView = delegate.performCreateActivityView(viewModel, viewBinder!!, this)
        setContentView(contentView)
        viewBinder!!.performViewBinding(viewModel, contentView)
    }

    override fun onDestroy() {
        viewBinder = null
        super.onDestroy()
    }

}