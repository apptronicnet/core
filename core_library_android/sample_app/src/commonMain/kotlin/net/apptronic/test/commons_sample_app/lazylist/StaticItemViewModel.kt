package net.apptronic.test.commons_sample_app.lazylist

import net.apptronic.core.context.Context
import net.apptronic.core.entity.commons.mutableValue
import net.apptronic.core.entity.function.map
import net.apptronic.core.viewmodel.EmptyViewModelContext
import net.apptronic.core.viewmodel.ViewModel

class StaticItemViewModel(parent: Context) : ViewModel(parent, EmptyViewModelContext) {

    val item = mutableValue<StaticItem>()

    val text = item.map {
        "${it.definition} ${it.text}"
    }

}