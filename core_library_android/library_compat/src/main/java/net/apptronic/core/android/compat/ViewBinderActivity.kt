package net.apptronic.core.android.compat

import android.os.Bundle
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.mvvm.viewmodel.ViewModel

abstract class ViewBinderActivity<T : ViewModel> : CoreCompatActivity<T>() {

    abstract fun buildViewBinder(): ViewBinder<T>

    private var viewBinder: ViewBinder<T>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinder = buildViewBinder()
        val contentView = viewBinder!!.onCreateActivityView(this)
        setContentView(contentView)
        viewBinder!!.performViewBinding(contentView, viewModel)
    }

    override fun onDestroy() {
        viewBinder = null
        super.onDestroy()
    }

}