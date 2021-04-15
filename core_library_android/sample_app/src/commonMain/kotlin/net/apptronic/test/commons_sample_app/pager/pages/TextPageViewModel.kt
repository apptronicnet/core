package net.apptronic.test.commons_sample_app.pager.pages

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.childContext
import net.apptronic.core.entity.commons.toggle
import net.apptronic.core.entity.commons.value
import net.apptronic.core.viewmodel.ViewModel
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

fun Contextual.textPageViewModel() = TextPageViewModel(childContext())

class TextPageViewModel(context: Context) : ViewModel(context) {

    private val pageNumber = inject(NextPageNumberDescriptor)

    val number = value("Text $pageNumber")

    val text = toggle(STRINGS, STRINGS.random())

    fun onTextClick() {
        text.toggle()
    }

}