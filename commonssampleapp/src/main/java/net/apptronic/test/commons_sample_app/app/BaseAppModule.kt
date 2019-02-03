package net.apptronic.test.commons_sample_app.app

import net.apptronic.common.core.component.di.declareModule
import net.apptronic.test.commons_sample_app.ToolbarTitled

val BaseAppModule = declareModule {

    single<ToolbarTitled> {
        val r = get<String>(name = "some")
        object : ToolbarTitled {
            override fun getToolbarTitle(): String {
                return ""
            }
        }
    }.onRecycle {

    }

}