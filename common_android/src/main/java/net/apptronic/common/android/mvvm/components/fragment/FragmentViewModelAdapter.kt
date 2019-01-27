package net.apptronic.common.android.mvvm.components.fragment

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import net.apptronic.common.core.mvvm.viewmodel.ViewModel
import net.apptronic.common.core.mvvm.viewmodel.adapter.ViewModelAdapter

abstract class FragmentViewModelAdapter(
    private val fragmentManager: FragmentManager,
    @IdRes private val containerViewId: Int
) : ViewModelAdapter() {

    override fun onInvalidate(oldModel: ViewModel?, newModel: ViewModel?, transitionInfo: Any?) {
        val oldFragment = fragmentManager.findFragmentById(containerViewId)
        val newFragment: Fragment? = if (newModel != null) {
            createFragment(newModel)?.also {
                (it as? ViewModelController)?.setViewModel(newModel)
            }
        } else {
            null
        }
        fragmentManager.beginTransaction().apply {
            setupTransition(oldModel, newModel, transitionInfo, this)
            if (newFragment != null) {
                replace(containerViewId, newFragment)
            } else {
                if (oldFragment != null) {
                    remove(oldFragment)
                }
            }
        }.commitNowAllowingStateLoss()
    }

    abstract fun createFragment(viewModel: ViewModel): Fragment?

    open fun setupTransition(
        oldModel: ViewModel?,
        newModel: ViewModel?,
        transitionInfo: Any?,
        fragmentTransaction: FragmentTransaction
    ) {
        // implement by subclass if needed
    }

}