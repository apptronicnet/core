package net.apptronic.test.commons_sample_app.pager.pages

import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.test.commons_sample_app.pager.NextPageNumberDescriptor
import kotlin.random.Random

private val STRINGS = listOf(
    "Hello world!",
    "Some things",
    "Random Access Memories",
    "Title and description",
    "Let's do it together",
    "Some thing are not changing",
    "Universe in infinite"
)

class TextPageViewModel(context: ViewModelContext) : ViewModel(context) {

    private val pageNumber = getProvider().inject(NextPageNumberDescriptor)

    val number = value("Text $pageNumber")

    val text = value(STRINGS[Random.nextInt(STRINGS.size)])

    private val toggle = toggle(text, *STRINGS.toTypedArray())

    fun onTextClick() {
        toggle.toggle()
    }

}