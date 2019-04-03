package net.apptronic.test.commons_sample_app.app

import net.apptronic.core.component.Component
import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.test.commons_sample_app.ApplicationScreenViewModel

class ApplicationComponent(context: Context) : Component(context) {

    private var appScreen: ApplicationScreenViewModel? = null

    fun getApplicationScreenModel(): ApplicationScreenViewModel {
        return appScreen ?: run {
            ApplicationScreenViewModel(createAppScreenContext())
                .apply {
                    appScreen = this
                }
        }
    }

    private fun createAppScreenContext(): ViewModelContext {
        return ViewModelContext(this)
    }

    fun applicationScreenClosed() {
        appScreen?.terminate()
        appScreen = null
    }

}