package net.apptronic.test.commons_sample_app.stackloading

import net.apptronic.core.component.di.createDescriptor

val StackRouterDescriptor = createDescriptor<StackRouter>()

interface StackRouter {

    fun navigatorAdd()

    fun navigatorReplace()

    fun navigatorReplaceAll()

}