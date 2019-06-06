package net.apptronic.test.commons_sample_app.pager.pages

import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

fun createTextPage(
    parent: Context
): TextPageViewModel {
    return TextPageViewModel(ViewModelContext(parent))
}

fun createImagePage(
    parent: Context
): ImagePageViewModel {
    return ImagePageViewModel(ViewModelContext(parent))
}