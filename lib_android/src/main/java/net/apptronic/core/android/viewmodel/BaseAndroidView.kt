package net.apptronic.core.android.viewmodel

import android.view.View
import net.apptronic.core.mvvm.viewmodel.ViewModel

abstract class BaseAndroidView<T : ViewModel>(
    val viewModel: T
) {

    internal var contentView: View? = null

    abstract fun onBindView()

    fun getView(): View {
        return contentView
            ?: throw IllegalStateException("Should not call getView() when view is not created")
    }

    fun <T : View> findView(id: Int): T {
        return getView().findViewById<T>(id)!!
    }

}