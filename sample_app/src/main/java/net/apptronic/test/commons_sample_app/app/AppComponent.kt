package net.apptronic.test.commons_sample_app.app

import net.apptronic.common.core.component.Component
import net.apptronic.common.core.component.ComponentContext
import net.apptronic.common.core.component.di.parameters
import net.apptronic.test.commons_sample_app.app.di.SomeInterface

class AppComponent(context: ComponentContext) : Component(context) {

    val someInterface = objects().get<SomeInterface>(
        params = parameters {
            add(instance = "Name")
        }
    )

}