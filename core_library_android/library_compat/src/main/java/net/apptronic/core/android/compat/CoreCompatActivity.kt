package net.apptronic.core.android.compat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.navigation.ViewModelLifecycleController

abstract class CoreCompatActivity<T : ViewModel> : AppCompatActivity(),
    ICoreCompatActivity<T> {

    lateinit var lifecycleController: ViewModelLifecycleController

    override val viewModel: T by lazy {
        ViewModelProvider(
            this,
            ModelHolderViewModelFactory {
                buildViewModel(getParentContext())
            }
        ).get(CompatModelHolderViewModel::class.java).coreViewModel as T
    }

    override val componentContext: ViewModelContext
        get() = viewModel.context

    abstract fun buildViewModel(parent: Context): T

    override fun onCreate(savedInstanceState: Bundle?) {
        lifecycleController = ViewModelLifecycleController(viewModel)
        lifecycleController.setCreated(true)
        onViewModelCreated(savedInstanceState)
        super.onCreate(savedInstanceState)
        lifecycleController.setBound(true)
        onBindViewModel(savedInstanceState)
    }

    open fun onViewModelCreated(savedInstanceState: Bundle?) {
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

    open fun onViewModeDestroyed() {
        // implement by subclasses before binding is performed
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleController.setBound(false)
        onViewModelUnbound()
        lifecycleController.setCreated(false)
        onViewModeDestroyed()
    }

}