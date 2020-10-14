package net.apptronic.core.ios.viewmodel.navigation

import net.apptronic.core.ios.viewmodel.ViewBinder
import net.apptronic.core.ios.viewmodel.ViewBinderFactory
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.SingleViewModelAdapter
import net.apptronic.core.mvvm.viewmodel.navigation.TransitionInfo
import platform.UIKit.UIView
import platform.UIKit.addSubview
import platform.UIKit.removeFromSuperview

class SingleViewBinderAdapter(
        private val container: UIView,
        private val viewBinderFactory: ViewBinderFactory
) : SingleViewModelAdapter {

    private var currentBinder: ViewBinder<*, *>? = null

    override fun onInvalidate(newModel: IViewModel?, transitionInfo: TransitionInfo) {
        val newBinder =
                if (newModel != null) viewBinderFactory.getBinder(newModel) else null
        if (newBinder != null && newModel != null) {
            val view = newBinder.onCreateView()
            newBinder.performViewBinding(newModel, view)
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