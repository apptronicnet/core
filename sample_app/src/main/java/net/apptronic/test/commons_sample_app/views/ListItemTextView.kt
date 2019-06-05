package net.apptronic.test.commons_sample_app.views

import android.view.View
import kotlinx.android.synthetic.main.list_item_text.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.bindings.sendClicksTo
import net.apptronic.core.android.viewmodel.bindings.setTextFrom
import net.apptronic.core.component.entity.functions.variants.map
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.list.ListItemTextViewModel

class ListItemTextView : AndroidView<ListItemTextViewModel>() {

    override var layoutResId: Int? = R.layout.list_item_text

    override fun onBindView(view: View, viewModel: ListItemTextViewModel) {
        with(view) {
            +(listItemTxtIndex setTextFrom viewModel.index.map { it.toString() })
            +(listItemTxtContent setTextFrom viewModel.text)
            +(view sendClicksTo viewModel::onBodyClick)
            +(listItemTxtRemove sendClicksTo viewModel::onRemoveButtonClick)
        }
    }

}