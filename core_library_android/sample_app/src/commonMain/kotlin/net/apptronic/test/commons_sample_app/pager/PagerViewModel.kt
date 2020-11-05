package net.apptronic.test.commons_sample_app.pager

import net.apptronic.core.context.Contextual
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.ViewModelContext
import net.apptronic.core.viewmodel.navigation.listNavigator
import net.apptronic.core.viewmodel.viewModelContext
import net.apptronic.test.commons_sample_app.pager.pages.ImagePageViewModel
import net.apptronic.test.commons_sample_app.pager.pages.TextPageViewModel

fun Contextual.pagerViewModel() = PagerViewModel(viewModelContext(PagerContext))

class PagerViewModel internal constructor(context: ViewModelContext) : ViewModel(context) {

    val pages = listNavigator()

    init {
        pages.update { list ->
            list.add(TextPageViewModel(this.context))
            list.add(TextPageViewModel(this.context))
            list.add(TextPageViewModel(this.context))
        }
    }

    fun addTextStart() {
        pages.update { list ->
            list.add(0, TextPageViewModel(this.context))
        }
    }

    fun addTextEnd() {
        pages.update { list ->
            list.add(TextPageViewModel(this.context))
        }
    }

    fun addImageStart() {
        pages.update { list ->
            list.add(0, ImagePageViewModel(this.context))
        }
    }

    fun addImageEnd() {
        pages.update { list ->
            list.add(ImagePageViewModel(this.context))
        }
    }

}