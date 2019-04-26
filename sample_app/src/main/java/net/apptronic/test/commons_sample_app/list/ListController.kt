package net.apptronic.test.commons_sample_app.list

import net.apptronic.core.mvvm.viewmodel.ViewModel

interface ListController {

    fun onRemoveRequest(viewModel: ViewModel)

    fun onVisible(name: String)

    fun onNotVisible(name: String)

}