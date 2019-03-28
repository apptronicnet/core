package net.apptronic.test.commons_sample_app.app

import android.app.Application
import android.content.Context
import android.util.Log
import net.apptronic.core.android.component.AndroidMainThreadWorker
import net.apptronic.core.component.threading.ContextWorkers

class SampleAndroidApplication : Application() {

    private val coreContext = ApplicationContext(
        object : HttpClientFactory {
            override fun createHttpClient(): HttpClient {
                return object : HttpClient {}
            }
        },
        Platform.Android
    ).apply {
        setLogging { Log.i("ComponentContext", it) }
    }
    val appComponent by lazy {
        ApplicationComponent(coreContext)
    }

    override fun onCreate() {
        super.onCreate()
        coreContext.workers().assignWorker(
            ContextWorkers.UI,
            AndroidMainThreadWorker
        )
        coreContext.workers().assignWorker(
            ContextWorkers.DEFAULT,
            AndroidMainThreadWorker
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

