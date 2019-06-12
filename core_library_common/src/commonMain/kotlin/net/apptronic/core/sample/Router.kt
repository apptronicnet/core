package net.apptronic.core.sample

import net.apptronic.core.component.di.createDescriptor

val RouterDescriptor = createDescriptor<Router>()

interface Router {

    fun goToSecondPage()

    fun goBack()

}