package net.apptronic.test.commons_sample_app.list

import net.apptronic.core.base.concurrent.AtomicEntity
import net.apptronic.core.component.di.declareModule
import net.apptronic.core.component.di.dependencyDescriptor
import net.apptronic.core.mvvm.viewmodel.defineViewModelContext

val ListControllerDescriptor = dependencyDescriptor<ListController>()
val ListItemSerialGeneratorDescriptor = dependencyDescriptor<AtomicEntity<Int>>()
val GenerateNextListItemIndexDescriptor = dependencyDescriptor<Int>()
val ListItemTextViewModelDescriptor = dependencyDescriptor<ListItemTextViewModel>()
val ListItemImageViewModelDescriptor = dependencyDescriptor<ListItemImageViewModel>()

val listContext = defineViewModelContext {
    dependencyDispatcher.addModule(listModule)
}

private val listModule = declareModule {

    single(ListItemSerialGeneratorDescriptor) {
        AtomicEntity(1)
    }

    factory(GenerateNextListItemIndexDescriptor) {
        val entity = inject(ListItemSerialGeneratorDescriptor)
        entity.perform {
            val value = get()
            set(value + 1)
            value
        }
    }

    factory(ListItemTextViewModelDescriptor) {
        val index = inject(GenerateNextListItemIndexDescriptor)
        ListItemTextViewModel(index, providedContext())
    }

    factory(ListItemImageViewModelDescriptor) {
        val index = inject(GenerateNextListItemIndexDescriptor)
        ListItemImageViewModel(index, providedContext())
    }

}