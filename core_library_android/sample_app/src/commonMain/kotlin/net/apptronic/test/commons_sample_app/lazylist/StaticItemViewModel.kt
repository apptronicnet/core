package net.apptronic.test.commons_sample_app.lazylist

import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

class StaticItemViewModel(context: ViewModelContext) : ViewModel(context) {

    val item = mutableValue<StaticItem>()

    val text = item.map {
        "${it.definition} ${it.text}"
    }

}