package net.apptronic.test.commons_sample_app.app

import android.app.Application
import android.content.Context
import android.util.Log
import net.apptronic.core.android.plugins.installViewFactoryPlugin
import net.apptronic.core.plugins.installViewModelLogPlugin
import net.apptronic.test.commons_sample_app.AppViewFactory

class SampleAndroidApplication : Application() {

    val appComponent by lazy {
        ApplicationComponent(
            applicationContext(
                object : HttpClientFactory {
                    override fun createHttpClient(): HttpClient {
                        return object : HttpClient {}
                    }
                },
                PlatformDefinition.Android
            ).apply {
                installViewModelLogPlugin {
                    Log.i("ViewModelLog", it)
                }
                installViewFactoryPlugin(AppViewFactory)
            }
        )
    }

}

fun Context.getApplicationComponent(): ApplicationComponent {
    return (applicationContext as SampleAndroidApplication).appComponent
}
