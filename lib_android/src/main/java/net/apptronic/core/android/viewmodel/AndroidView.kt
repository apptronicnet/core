package net.apptronic.core.android.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.apptronic.core.mvvm.viewmodel.ViewModel

abstract class AndroidView<T : ViewModel>(
    val viewModel: T
) {

    private var contentView: View? = null

    internal fun bindView(container: ViewGroup) {
        val inflater = LayoutInflater.from(container.context)
        contentView = onCreateView(inflater, container)
        onBindView()
    }

    abstract fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View

    abstract fun onBindView()

    fun getView(): View {
        return contentView
            ?: throw IllegalStateException("Should not call getView() when view is not created")
    }

    fun <T : View> findView(id: Int): T {
        return getView().findViewById<T>(id)!!
    }

}