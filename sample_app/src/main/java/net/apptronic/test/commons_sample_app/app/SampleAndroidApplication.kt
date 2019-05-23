package net.apptronic.test.commons_sample_app.app

import android.app.Application
import android.content.Context
import net.apptronic.core.android.platform.AndroidPlatform

class SampleAndroidApplication : Application() {

    private val coreContext = ApplicationContext(
            object : HttpClientFactory {
                override fun createHttpClient(): HttpClient {
                    return object : HttpClient {}
                }
            },
            AndroidPlatform(),
            PlatformDefinition.Android
    )
    val appComponent by lazy {
        ApplicationComponent(coreContext)
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

