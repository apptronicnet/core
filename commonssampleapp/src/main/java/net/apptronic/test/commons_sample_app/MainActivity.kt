package net.apptronic.test.commons_sample_app

import android.os.Bundle
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
    }
}
