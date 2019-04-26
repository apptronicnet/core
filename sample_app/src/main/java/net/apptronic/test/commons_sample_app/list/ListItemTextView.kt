package net.apptronic.test.commons_sample_app.list

import android.view.View
import kotlinx.android.synthetic.main.list_item_text.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.ViewModelBinding
import net.apptronic.core.android.viewmodel.bindings.ClickActionBinding
import net.apptronic.core.android.viewmodel.bindings.TextBinding
import net.apptronic.core.component.entity.functions.variants.map
import net.apptronic.test.commons_sample_app.R

class ListItemTextView : AndroidView<ListItemTextViewModel>() {

    init {
        layoutResId = R.layout.list_item_text
    }

    override fun onCreateBinding(
        view: View,
        viewModel: ListItemTextViewModel
    ): ViewModelBinding<ListItemTextViewModel> {
        return createBinding(view, viewModel) {
            with(view) {
                listItemTxtIndex.bindTo(TextBinding(), viewModel.index.map { it.toString() })
                listItemTxtContent.bindTo(TextBinding(), viewModel.text)
                view.bindTo(ClickActionBinding(), viewModel::onBodyClick)
                listItemTxtRemove.bindTo(ClickActionBinding(), viewModel::onRemoveButtonClick)
            }
        }
    }

}