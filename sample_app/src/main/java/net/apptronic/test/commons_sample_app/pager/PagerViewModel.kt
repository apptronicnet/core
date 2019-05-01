package net.apptronic.test.commons_sample_app.pager

import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.test.commons_sample_app.pager.pages.createImagePage
import net.apptronic.test.commons_sample_app.pager.pages.createTextPage

class PagerViewModel(context: ViewModelContext) : ViewModel(context) {

    val pages = listNavigator()

    init {
        pages.update { list ->
            list.add(createTextPage(this))
            list.add(createTextPage(this))
            list.add(createTextPage(this))
        }
    }

    fun addTextStart() {
        pages.update { list ->
            list.add(0, createTextPage(this))
        }
    }

    fun addTextEnd() {
        pages.update { list ->
            list.add(createTextPage(this))
        }
    }

    fun addImageStart() {
        pages.update { list ->
            list.add(0, createImagePage(this))
        }
    }

    fun addImageEnd() {
        pages.update { list ->
            list.add(createImagePage(this))
        }
    }

}