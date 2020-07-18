package net.apptronic.core.android.compat

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.navigation.ViewModelLifecycleController

abstract class CoreCompatDialogFragment<T : ViewModel> : DialogFragment(), ICoreCompatFragment<T> {

    lateinit var lifecycleController: ViewModelLifecycleController

    override val viewModel: T by lazy {
        ViewModelProvider(
            this,
            ModelHolderViewModelFactory {
                buildViewModel(getParentContext())
            }
        ).get(CompatModelHolderViewModel::class.java).coreViewModel as T
    }

    abstract fun buildViewModel(parent: Context): T

    override val componentContext: ViewModelContext
        get() = viewModel.context

    override fun onCreate(savedInstanceState: Bundle?) {
        lifecycleController = ViewModelLifecycleController(viewModel)
        lifecycleController.setAttached(true)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleController.setBound(true)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        lifecycleController.setVisible(true)
    }

    override fun onResume() {
        super.onResume()
        lifecycleController.setFocused(true)
    }

    override fun onPause() {
        super.onPause()
        lifecycleController.setFocused(false)
    }

    override fun onStop() {
        super.onStop()
        lifecycleController.setVisible(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        lifecycleController.setBound(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleController.setAttached(false)
    }

}