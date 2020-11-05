package net.apptronic.test.commons_sample_app.binders

import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindClickListener
import net.apptronic.core.android.viewmodel.bindings.bindText
import net.apptronic.core.entity.function.map
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.databinding.ListItemTextBinding
import net.apptronic.test.commons_sample_app.list.ListItemTextViewModel

class ListItemTextViewBinder : ViewBinder<ListItemTextViewModel>() {

    override var layoutResId: Int? = R.layout.list_item_text

    override fun onBindView() = withBinging(ListItemTextBinding::bind) {
        bindText(listItemTxtIndex, viewModel.index.map { it.toString() })
        bindText(listItemTxtContent, viewModel.text)
        bindClickListener(view, viewModel::onBodyClick)
        bindClickListener(listItemTxtRemove, viewModel::onRemoveButtonClick)
    }

}