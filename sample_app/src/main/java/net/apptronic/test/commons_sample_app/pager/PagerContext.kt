package net.apptronic.test.commons_sample_app.pager

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.di.createDescriptor
import net.apptronic.core.component.di.declareModule
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import java.util.concurrent.atomic.AtomicInteger

class PagerContext(parent: Context) : ViewModelContext(parent) {

    init {
        getProvider().addModule(PagerModule)
    }

}

private val NumberGeneratorDescriptor = createDescriptor<AtomicInteger>()
val NextPageNumberDescriptor = createDescriptor<Int>()

private val PagerModule = declareModule {

    single(NumberGeneratorDescriptor) {
        AtomicInteger(1)
    }

    factory(NextPageNumberDescriptor) {
        inject(NumberGeneratorDescriptor).getAndIncrement()
    }

}