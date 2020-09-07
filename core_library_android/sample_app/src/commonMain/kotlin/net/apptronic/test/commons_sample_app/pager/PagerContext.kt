package net.apptronic.test.commons_sample_app.pager

import net.apptronic.core.base.SerialIdGenerator
import net.apptronic.core.component.di.declareModule
import net.apptronic.core.component.di.dependencyDescriptor
import net.apptronic.core.mvvm.viewmodel.defineViewModelContext

val PagerContext = defineViewModelContext("PagerContext") {
    dependencyDispatcher.addModule(PagerModule)
}

private val NumberGeneratorDescriptor = dependencyDescriptor<SerialIdGenerator>()
val NextPageNumberDescriptor = dependencyDescriptor<Int>()

private val PagerModule = declareModule {

    single(NumberGeneratorDescriptor) {
        SerialIdGenerator()
    }

    factory(NextPageNumberDescriptor) {
        inject(NumberGeneratorDescriptor).nextId().toInt()
    }

}