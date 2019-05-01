package net.apptronic.test.commons_sample_app.pager

import net.apptronic.core.component.context.Context

fun createPagerViewModel(
    parent: Context
): PagerViewModel {
    return PagerViewModel(PagerContext(parent))
}