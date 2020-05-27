package net.apptronic.test.commons_sample_app.views

import android.view.View
import kotlinx.android.synthetic.main.list_item_text.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.bindings.bindClickListener
import net.apptronic.core.android.viewmodel.bindings.bindText
import net.apptronic.core.component.entity.functions.map
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.list.ListItemTextViewModel

class ListItemTextView : AndroidView<ListItemTextViewModel>() {

    override var layoutResId: Int? = R.layout.list_item_text

    override fun onBindView(view: View, viewModel: ListItemTextViewModel) {
        with(view) {
            bindText(listItemTxtIndex, viewModel.index.map { it.toString() })
            bindText(listItemTxtContent, viewModel.text)
            bindClickListener(view, viewModel::onBodyClick)
            bindClickListener(listItemTxtRemove, viewModel::onRemoveButtonClick)
        }
    }

}