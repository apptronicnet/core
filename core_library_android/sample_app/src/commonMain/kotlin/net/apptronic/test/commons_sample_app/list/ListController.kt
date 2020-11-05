package net.apptronic.test.commons_sample_app.list

import net.apptronic.core.viewmodel.IViewModel

interface ListController {

    fun onRemoveRequest(viewModel: IViewModel)

    fun onVisible(name: String)

    fun onNotVisible(name: String)

}