package net.apptronic.test.commons_sample_app.pager

import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.listNavigator
import net.apptronic.test.commons_sample_app.pager.pages.ImagePageViewModel
import net.apptronic.test.commons_sample_app.pager.pages.TextPageViewModel

class PagerViewModel(parent: Context) : ViewModel(parent, PagerContext) {

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