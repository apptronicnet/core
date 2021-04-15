package net.apptronic.test.commons_sample_app.lazylist

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.childContext
import net.apptronic.core.entity.commons.mutableValue
import net.apptronic.core.entity.function.map
import net.apptronic.core.viewmodel.ViewModel

fun Contextual.staticItemViewModel() = StaticItemViewModel(childContext())

class StaticItemViewModel(context: Context) : ViewModel(context) {

    val item = mutableValue<StaticItem>()

    val text = item.map {
        "${it.definition} ${it.text}"
    }

}