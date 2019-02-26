package net.apptronic.test.commons_sample_app.app

import android.app.Application
import net.apptronic.common.core.component.base.CoreComponentContext
import net.apptronic.test.commons_sample_app.app.di.BaseAppModule

class SampleAndroidApplication : Application() {

    private val coreContext = CoreComponentContext()
    private val appComponent = AppComponent(coreContext)

    override fun onCreate() {
        super.onCreate()
//        coreContext.workers().assignWorker(ContextWorkers.UI, AndroidMainThreadWorker)
//        coreContext.workers().assignWorker(ContextWorkers.DEFAULT, AndroidMainThreadWorker)
        coreContext.objects().addModule(BaseAppModule)
    }

}