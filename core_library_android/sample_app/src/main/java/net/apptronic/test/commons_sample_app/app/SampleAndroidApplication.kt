package net.apptronic.test.commons_sample_app.app

import android.app.Application
import android.content.Context
import android.util.Log
import net.apptronic.core.android.plugins.installAndroidApplicationPlugin
import net.apptronic.core.plugins.installViewModelLogPlugin
import net.apptronic.test.commons_sample_app.AppBinderFactory
import net.apptronic.test.commons_sample_app.AppTransitionBuilder
import net.apptronic.test.commons_sample_app.ApplicationScreenViewModel
import net.apptronic.test.commons_sample_app.MainActivity

class SampleAndroidApplication : Application() {

    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = ApplicationComponent(
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
                installAndroidApplicationPlugin(this@SampleAndroidApplication) {
                    binderFactory(AppBinderFactory)
                    transitionBuilder(AppTransitionBuilder())
                    bindActivity(MainActivity::class, ApplicationScreenViewModel::class) {
                        it.onBackPressed()
                    }
                }
            }
        )
    }

}

fun Context.getApplicationComponent(): ApplicationComponent {
    return (applicationContext as SampleAndroidApplication).appComponent
}
