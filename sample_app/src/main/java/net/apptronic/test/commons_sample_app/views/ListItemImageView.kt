package net.apptronic.test.commons_sample_app.views

import android.view.View
import kotlinx.android.synthetic.main.list_item_image.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.bindings.sendClicksTo
import net.apptronic.core.android.viewmodel.bindings.setImageResourceFrom
import net.apptronic.core.android.viewmodel.bindings.setImageTintFrom
import net.apptronic.core.android.viewmodel.bindings.setTextFrom
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.list.ListItemImageViewModel

class ListItemImageView : AndroidView<ListItemImageViewModel>() {

    init {
        layoutResId = R.layout.list_item_image
    }

    override fun onBindView(view: View, viewModel: ListItemImageViewModel) {
        with(view) {
            +(listItemImgIndex setTextFrom viewModel.index.map { it.toString() })
            +(listItemImgContent setImageResourceFrom viewModel.imageRes.map { it.getResourceId() })
            +(listItemImgContent setImageTintFrom viewModel.colorRes.map { it.getResourceId() }.resourceToColor(context))
            +(view sendClicksTo viewModel::onBodyClick)
            +(listItemImgRemove sendClicksTo viewModel::onRemoveButtonClick)
        }
    }

}