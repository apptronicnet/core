package net.apptronic.test.commons_sample_app.binders

import android.view.View
import kotlinx.android.synthetic.main.list_item_image.view.*
import net.apptronic.core.android.component.functions.resourceToColor
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindClickListener
import net.apptronic.core.android.viewmodel.bindings.bindImageResource
import net.apptronic.core.android.viewmodel.bindings.bindImageTint
import net.apptronic.core.android.viewmodel.bindings.bindText
import net.apptronic.core.component.entity.functions.map
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.list.ListItemImageViewModel
import net.apptronic.test.commons_sample_app.resources.getResourceId

class ListItemImageViewBinder : ViewBinder<ListItemImageViewModel>() {

    override var layoutResId: Int? = R.layout.list_item_image

    override fun onBindView(view: View, viewModel: ListItemImageViewModel) {
        with(view) {
            bindText(listItemImgIndex, viewModel.index.map { it.toString() })
            bindImageResource(listItemImgContent, viewModel.imageRes.map { it.getResourceId() })
            bindImageTint(
                listItemImgContent,
                viewModel.colorRes.map { it.getResourceId() }.resourceToColor(context)
            )
            bindClickListener(view, viewModel::onBodyClick)
            bindClickListener(listItemImgRemove, viewModel::onRemoveButtonClick)
        }
    }

}