package net.apptronic.core.ios.viewmodel.navigation

import net.apptronic.core.ios.viewmodel.ViewBinder
import net.apptronic.core.ios.viewmodel.ViewBinderFactory
import net.apptronic.core.viewmodel.navigation.TransitionInfo
import net.apptronic.core.viewmodel.navigation.ViewModelItem
import net.apptronic.core.viewmodel.navigation.adapters.SingleViewModelAdapter
import platform.UIKit.UIView
import platform.UIKit.addSubview
import platform.UIKit.removeFromSuperview

class SingleViewBinderAdapter(
        private val container: UIView,
        private val viewBinderFactory: ViewBinderFactory
) : SingleViewModelAdapter {

    private var currentBinder: ViewBinder<*, *>? = null

    override fun onInvalidate(item: ViewModelItem?, transitionInfo: TransitionInfo) {
        val viewModel = item?.viewModel
        val newBinder =
                if (viewModel != null) viewBinderFactory.getBinder(viewModel) else null
        if (newBinder != null && viewModel != null) {
            val view = newBinder.onCreateView()
            newBinder.performViewBinding(viewModel, view)
        }
        setView(newBinder, transitionInfo.isNewOnFront, transitionInfo.spec)
    }

    private fun setView(newBinder: ViewBinder<*, *>?, isNewOnFront: Boolean, transitionSpec: Any?) {
        val oldBinder = currentBinder
        oldBinder?.getView()?.view?.removeFromSuperview()
        currentBinder = newBinder
        if (newBinder != null) {
            val content = newBinder.getView().view
            container.addSubview(content)
        }

    }

}