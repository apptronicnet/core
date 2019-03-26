package net.apptronic.test.commons_sample_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import net.apptronic.test.commons_sample_app.app.lazyApplicationComponent

class MainActivity : AppCompatActivity() {

    private val appComponent by lazyApplicationComponent()
    private val viewModel by lazy {
        appComponent.getApplicationScreenModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.mainScreen.setAdapter(MainModelAdapter(container))
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            appComponent.applicationScreenClosed()
        }
    }

}
