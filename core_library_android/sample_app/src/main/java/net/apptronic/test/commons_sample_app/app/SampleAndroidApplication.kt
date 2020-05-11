package net.apptronic.test.commons_sample_app.app

import android.app.Application
import android.content.Context
import net.apptronic.core.android.platform.AndroidPlatform
import net.apptronic.core.platform.initializePlatform

class SampleAndroidApplication : Application() {

    init {
        initializePlatform(AndroidPlatform)
    }

    val appComponent by lazy {
        ApplicationComponent(
            applicationContext(
                object : HttpClientFactory {
                    override fun createHttpClient(): HttpClient {
                        return object : HttpClient {}
                    }
                },
                PlatformDefinition.Android
            )
        )
    }

}

fun Context.getApplicationComponent(): ApplicationComponent {
    return (applicationContext as SampleAndroidApplication).appComponent
}
