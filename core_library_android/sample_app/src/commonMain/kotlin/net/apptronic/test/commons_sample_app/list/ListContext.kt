package net.apptronic.test.commons_sample_app.list

import net.apptronic.core.base.SerialIdGenerator
import net.apptronic.core.context.di.declareModule
import net.apptronic.core.context.di.dependencyDescriptor

val ListControllerDescriptor = dependencyDescriptor<ListController>()
val ListItemSerialGeneratorDescriptor = dependencyDescriptor<SerialIdGenerator>()
val GenerateNextListItemIndexDescriptor = dependencyDescriptor<Int>()
val ListItemTextViewModelDescriptor = dependencyDescriptor<ListItemTextViewModel>()
val ListItemImageViewModelDescriptor = dependencyDescriptor<ListItemImageViewModel>()

val ListModule = declareModule {

    single(ListItemSerialGeneratorDescriptor) {
        SerialIdGenerator()
    }

    factory(GenerateNextListItemIndexDescriptor) {
        val generator = inject(ListItemSerialGeneratorDescriptor)
        generator.nextId().toInt()
    }

    factory(ListItemTextViewModelDescriptor) {
        val index = inject(GenerateNextListItemIndexDescriptor)
        providedContext().listItemTextViewModel(index)
    }

    factory(ListItemImageViewModelDescriptor) {
        val index = inject(GenerateNextListItemIndexDescriptor)
        providedContext().listItemImageViewModel(index)
    }

}