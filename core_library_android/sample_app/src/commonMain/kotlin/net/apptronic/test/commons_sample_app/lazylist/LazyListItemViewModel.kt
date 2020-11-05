package net.apptronic.test.commons_sample_app.lazylist

import net.apptronic.core.context.Context
import net.apptronic.core.context.di.dependencyDescriptor
import net.apptronic.core.entity.commons.genericEvent
import net.apptronic.core.entity.commons.value
import net.apptronic.core.entity.function.map
import net.apptronic.core.viewmodel.EmptyViewModelContext
import net.apptronic.core.viewmodel.ViewModel

val LazyListItemClickListenerDescriptor = dependencyDescriptor<LazyListItemClickListener>()

interface LazyListItemClickListener {

    fun onClick(id: Long)

}

class LazyListItemViewModel(
    parent: Context
) : ViewModel(parent, EmptyViewModelContext) {

    private val listener = inject(LazyListItemClickListenerDescriptor)

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