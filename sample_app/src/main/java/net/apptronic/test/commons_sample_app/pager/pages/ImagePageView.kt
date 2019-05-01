package net.apptronic.test.commons_sample_app.pager.pages

import android.view.View
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.page_image.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.bindings.sendClicksTo
import net.apptronic.core.android.viewmodel.bindings.setImageResourceFrom
import net.apptronic.core.android.viewmodel.bindings.setImageTintFrom
import net.apptronic.core.android.viewmodel.bindings.setTextFrom
import net.apptronic.core.component.entity.functions.variants.map
import net.apptronic.test.commons_sample_app.R

class ImagePageView : AndroidView<ImagePageViewModel>() {

    init {
        layoutResId = R.layout.page_image
    }

    override fun onBindView(view: View, viewModel: ImagePageViewModel) {
        with(view) {
            +(imagePageNumber setTextFrom viewModel.number)
            +(imagePageValue setImageResourceFrom viewModel.imageRes)
            +(imagePageValue setImageTintFrom viewModel.colorRes.map {
                ContextCompat.getColor(
                    view.context,
                    it
                )
            })
            +(imagePageValue sendClicksTo viewModel::onImageClick)
        }
    }

}