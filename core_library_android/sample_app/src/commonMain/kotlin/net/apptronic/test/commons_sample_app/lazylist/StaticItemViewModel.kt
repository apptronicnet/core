package net.apptronic.test.commons_sample_app.lazylist

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.component.mutableValue
import net.apptronic.core.mvvm.viewmodel.EmptyViewModelContext
import net.apptronic.core.mvvm.viewmodel.ViewModel

class StaticItemViewModel(parent: Context) : ViewModel(parent, EmptyViewModelContext) {

    val item = mutableValue<StaticItem>()

    val text = item.map {
        "${it.definition} ${it.text}"
    }

}