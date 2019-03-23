package net.apptronic.test.commons_sample_app.app

import net.apptronic.core.component.Component
import net.apptronic.core.component.context.ComponentContext
import net.apptronic.core.component.di.parameters
import net.apptronic.test.commons_sample_app.app.di.SomeInterface

class AppComponent(context: ComponentContext) : Component(context) {

    val someInterface = objects().get<SomeInterface>(
        params = parameters {
            add(instance = "Name")
        }
    )

}