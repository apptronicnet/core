package net.apptronic.test.commons_sample_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import net.apptronic.core.mvvm.viewmodel.container.ViewModelLifecycleController
import net.apptronic.test.commons_sample_app.app.lazyApplicationComponent

class MainActivity : AppCompatActivity() {

    private val appComponent by lazyApplicationComponent()
    private val viewModel by lazy {
        appComponent.getApplicationScreenModel()
    }
    private val lifecycleController by lazy {
        ViewModelLifecycleController(viewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleController.setCreated(true)
        setContentView(R.layout.activity_main)
        viewModel.rootScreen.setAdapter(MainModelAdapter(container))
        lifecycleController.setBound(true)
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

    override fun onBackPressed() {
        viewModel.onBackPressed {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleController.setBound(false)
        viewModel.rootScreen.setAdapter(null)
        if (isFinishing) {
            lifecycleController.setCreated(false)
            appComponent.applicationScreenClosed()
        }
    }

}
