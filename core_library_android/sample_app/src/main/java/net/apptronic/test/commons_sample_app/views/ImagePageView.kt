package net.apptronic.test.commons_sample_app.views

import android.view.View
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.page_image.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.bindings.*
import net.apptronic.core.component.entity.functions.map
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.pager.pages.ImagePageViewModel
import net.apptronic.test.commons_sample_app.resources.getResourceId

class ImagePageView : AndroidView<ImagePageViewModel>() {

    override var layoutResId: Int? = R.layout.page_image

    override fun onBindView(view: View, viewModel: ImagePageViewModel) {
        with(view) {
            bindText(imagePageNumber, viewModel.number)
            bindImageResource(imagePageValue, viewModel.imageRes.map { it.getResourceId() })
            bindImageTint(imagePageValue, viewModel.colorRes.map {
                ContextCompat.getColor(view.context, it.getResourceId())
            })
            bindClickListener(imagePageValue, viewModel::onImageClick)
        }
    }

}