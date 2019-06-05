package net.apptronic.test.commons_sample_app.pager

import net.apptronic.core.base.concurrent.AtomicEntity
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.di.createDescriptor
import net.apptronic.core.component.di.declareModule
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

class PagerContext(parent: Context) : ViewModelContext(parent) {

    init {
        getProvider().addModule(PagerModule)
    }

}

private val NumberGeneratorDescriptor = createDescriptor<AtomicEntity<Int>>()
val NextPageNumberDescriptor = createDescriptor<Int>()

private val PagerModule = declareModule {

    single(NumberGeneratorDescriptor) {
        AtomicEntity(1)
    }

    factory(NextPageNumberDescriptor) {
        inject(NumberGeneratorDescriptor).perform {
            val res = get()
            set(get() + 1)
            res
        }
    }

}