package net.apptronic.test.commons_sample_app.lazylist

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.di.dependencyDescriptor
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.component.genericEvent
import net.apptronic.core.component.inject
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.EmptyViewModelContext
import net.apptronic.core.mvvm.viewmodel.ViewModel

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