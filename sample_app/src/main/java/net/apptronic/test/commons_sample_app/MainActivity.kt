package net.apptronic.test.commons_sample_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import net.apptronic.core.android.viewmodel.ActivityViewModelLifecycleController
import net.apptronic.test.commons_sample_app.app.lazyApplicationComponent

class MainActivity : AppCompatActivity() {

    private val appComponent by lazyApplicationComponent()
    private val viewModel by lazy {
        appComponent.getApplicationScreenModel()
    }
    private val lifecycleController by lazy {
        ActivityViewModelLifecycleController(MainActivityView(viewModel)) {
            container
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleController.setCreated(true)
        setContentView(R.layout.activity_main)
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
        viewModel.rootPage.setAdapter(null)
        if (isFinishing) {
            lifecycleController.setCreated(false)
            appComponent.applicationScreenClosed()
        }
    }

}
