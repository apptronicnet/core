package net.apptronic.core.android.compat

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import net.apptronic.core.context.Context
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.navigation.ViewModelLifecycleController

abstract class CoreCompatDialogFragment<T : IViewModel> : DialogFragment(), ICoreCompatFragment<T> {

    private lateinit var lifecycleController: ViewModelLifecycleController

    private lateinit var viewModelReference: T

    override val viewModel: T
        get() {
            return viewModelReference
        }

    abstract fun buildViewModel(parent: Context): T

    override val componentContext: Context
        get() = viewModel.context

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        viewModelReference = ViewModelProvider(
            this,
            ModelHolderViewModelFactory {
                buildViewModel(getParentContext())
            }
        ).get(CompatModelHolderViewModel::class.java).coreViewModel as T
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