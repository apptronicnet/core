package net.apptronic.test.commons_sample_app.views

import android.view.View
import kotlinx.android.synthetic.main.screen_pages.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.bindings.bindClickListener
import net.apptronic.core.android.viewmodel.bindings.navigation.bindPagerNavigator
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.pager.PagerViewModel

class PagerView : AndroidView<PagerViewModel>() {

    override var layoutResId: Int? = R.layout.screen_pages

    override fun onBindView(view: View, viewModel: PagerViewModel) {
        with(view) {
            bindPagerNavigator(viewPager, viewModel.pages)
            bindClickListener(addTextStart, viewModel::addTextStart)
            bindClickListener(addTextEnd, viewModel::addTextEnd)
            bindClickListener(addImageStart, viewModel::addImageStart)
            bindClickListener(addImageEnd, viewModel::addImageEnd)
        }
    }

}