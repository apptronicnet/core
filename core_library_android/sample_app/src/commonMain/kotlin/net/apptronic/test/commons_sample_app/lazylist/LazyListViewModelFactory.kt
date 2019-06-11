package net.apptronic.test.commons_sample_app.lazylist

import net.apptronic.core.component.context.Context

fun createLazyListItemViewModel(
    parent: Context
): LazyListViewModel {
    val context = LazyListContext(parent)
    return LazyListViewModel(context)
}