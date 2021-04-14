package net.apptronic.core.android.compat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import net.apptronic.core.context.Context
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.navigation.ViewModelLifecycleController

abstract class CoreCompatActivity<T : IViewModel> : AppCompatActivity(),
    ICoreCompatActivity<T> {

    lateinit var lifecycleController: ViewModelLifecycleController

    private lateinit var viewModelReference: T

    override val viewModel: T
        get() {
            return viewModelReference
        }

    override val componentContext: Context
        get() = viewModel.context

    abstract fun buildViewModel(parent: Context): T

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
        onViewModelAttached(savedInstanceState)
        super.onCreate(savedInstanceState)
        lifecycleController.setBound(true)
        onBindViewModel(savedInstanceState)
    }

    open fun onViewModelAttached(savedInstanceState: Bundle?) {
        // implement by subclasses before binding is performed
    }

    open fun onBindViewModel(savedInstanceState: Bundle?) {
        // implement by subclasses before binding is performed
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

    open fun onViewModelUnbound() {
        // implement by subclasses before binding is performed
    }

    open fun onViewModeDetached() {
        // implement by subclasses before binding is performed
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleController.setBound(false)
        onViewModelUnbound()
        lifecycleController.setAttached(false)
        onViewModeDetached()
    }

}