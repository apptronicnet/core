package net.apptronic.test.commons_sample_app.lazylist

import net.apptronic.core.component.di.createDescriptor
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

val LazyListItemClickListenerDescriptor = createDescriptor<LazyListItemClickListener>()

interface LazyListItemClickListener {

    fun onClick(id: Long)

}

class LazyListItemViewModel(
    context: ViewModelContext
) : ViewModel(context) {

    private val listener = getProvider().inject(LazyListItemClickListenerDescriptor)

    val item = value<LazyListItem>()

    val number = item.map {
        "${it.number}."
    }
    val text = item.map {
        it.text
    }

    val onClick = genericEvent {
        val id = item.get().id
        listener.onClick(id)
    }

}