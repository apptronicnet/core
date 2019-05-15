package net.apptronic.test.commons_sample_app.app

import android.app.Application
import android.content.Context
import android.util.Log
import net.apptronic.core.android.component.AndroidMainThreadWorkerProvider
import net.apptronic.core.threading.Scheduler

class SampleAndroidApplication : Application() {

    private val coreContext = ApplicationContext(
        object : HttpClientFactory {
            override fun createHttpClient(): HttpClient {
                return object : HttpClient {}
            }
        },
        Platform.Android
    ).apply {
        setLogging { Log.i("Context", it) }
    }
    val appComponent by lazy {
        ApplicationComponent(coreContext)
    }

    override fun onCreate() {
        super.onCreate()
        coreContext.getScheduler().assignWorker(
            Scheduler.UI,
            AndroidMainThreadWorkerProvider
        )
        coreContext.getScheduler().assignWorker(
            Scheduler.DEFAULT,
            AndroidMainThreadWorkerProvider
        )
    }

}

fun Context.getApplicationComponent(): ApplicationComponent {
    return (applicationContext as SampleAndroidApplication).appComponent
}

fun Context.lazyApplicationComponent(): Lazy<ApplicationComponent> {
    return lazy {
        getApplicationComponent()
    }
}

