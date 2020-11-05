package net.apptronic.test.commons_sample_app.stackloading

import net.apptronic.core.context.di.dependencyDescriptor

val StackRouterDescriptor = dependencyDescriptor<StackRouter>()

interface StackRouter {

    fun navigatorAdd()

    fun navigatorReplace()

    fun navigatorReplaceAll()

}