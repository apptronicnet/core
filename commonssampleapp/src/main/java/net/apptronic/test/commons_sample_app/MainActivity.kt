package net.apptronic.test.commons_sample_app

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import net.apptronic.common.android.ui.components.activity.ActivityLifecycle
import net.apptronic.common.android.ui.components.activity.BaseActivity
import net.apptronic.common.android.ui.threading.AndroidMainThreadExecutor

class MainActivity : BaseActivity<MainViewModel>() {

    override fun createViewModel(): MainViewModel? {
        return MainViewModel(ActivityLifecycle(AndroidMainThreadExecutor()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        model.toolbarTitle.subscribe {
            activityTitle.text = it
        }
        model.rootModel.setAdapter(RootModelAdapter(supportFragmentManager, R.id.fragmentContainer))
    }

    override fun onDestroy() {
        super.onDestroy()
        model.rootModel.setAdapter(null)
    }

    override fun onBackPressed() {
        if (!model.onBackPressed()) {
            super.onBackPressed()
        }
    }

}
