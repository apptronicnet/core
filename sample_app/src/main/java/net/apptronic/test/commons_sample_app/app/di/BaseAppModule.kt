package net.apptronic.test.commons_sample_app.app.di

import net.apptronic.core.component.di.declareModule
import net.apptronic.test.commons_sample_app.ToolbarTitled

val BaseAppModule = declareModule {

    single<ToolbarTitled> {
        val r = inject<String>(name = "some")
        object : ToolbarTitled {
            override fun getToolbarTitle(): String {
                return ""
            }
        }
    }.onRecycle {

    }

    single<SomeInterface> {
        SomeInterfaceImpl()
    }.onRecycle {
        (it as SomeInterfaceImpl).recycle()
    }

}