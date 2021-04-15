package net.apptronic.test.commons_sample_app.pager

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.childContext
import net.apptronic.core.context.dependencyModule
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.navigation.listNavigator
import net.apptronic.test.commons_sample_app.pager.pages.imagePageViewModel
import net.apptronic.test.commons_sample_app.pager.pages.textPageViewModel

fun Contextual.pagerViewModel() = PagerViewModel(
    childContext {
        dependencyModule(PagerModule)
    }
)

class PagerViewModel internal constructor(context: Context) : ViewModel(context) {

    val pages = listNavigator()

    init {
        pages.update { list ->
            list.add(textPageViewModel())
            list.add(textPageViewModel())
            list.add(textPageViewModel())
        }
    }

    fun addTextStart() {
        pages.update { list ->
            list.add(0, textPageViewModel())
        }
    }

    fun addTextEnd() {
        pages.update { list ->
            list.add(textPageViewModel())
        }
    }

    fun addImageStart() {
        pages.update { list ->
            list.add(0, imagePageViewModel())
        }
    }

    fun addImageEnd() {
        pages.update { list ->
            list.add(imagePageViewModel())
        }
    }

}