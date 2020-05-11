package net.apptronic.test.commons_sample_app.pager.pages

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.inject
import net.apptronic.core.component.toggle
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.EMPTY_VIEW_MODEL_CONTEXT
import net.apptronic.core.mvvm.viewmodel.ViewModel
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

class TextPageViewModel(parent: Context) : ViewModel(parent, EMPTY_VIEW_MODEL_CONTEXT) {

    private val pageNumber = inject(NextPageNumberDescriptor)

    val number = value("Text $pageNumber")

    val text = value(STRINGS[Random.nextInt(STRINGS.size)])

    private val toggle = toggle(text, *STRINGS.toTypedArray())

    fun onTextClick() {
        toggle.toggle()
    }

}