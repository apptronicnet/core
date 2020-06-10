package net.apptronic.test.commons_sample_app.pager.pages

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.inject
import net.apptronic.core.component.toggle
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.EmptyViewModelContext
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.test.commons_sample_app.pager.NextPageNumberDescriptor

private val STRINGS = listOf(
    "Hello world!",
    "Some things",
    "Random Access Memories",
    "Title and description",
    "Let's do it together",
    "Some thing are not changing",
    "Universe in infinite"
)

class TextPageViewModel(parent: Context) : ViewModel(parent, EmptyViewModelContext) {

    private val pageNumber = inject(NextPageNumberDescriptor)

    val number = value("Text $pageNumber")

    val text = toggle(STRINGS, STRINGS.random())

    fun onTextClick() {
        text.toggle()
    }

}