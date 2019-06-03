package net.apptronic.test.commons_sample_app.views

import android.view.View
import kotlinx.android.synthetic.main.screen_pages.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.bindings.navigation.pagerNavigatorBinding
import net.apptronic.core.android.viewmodel.bindings.sendClicksTo
import net.apptronic.test.commons_sample_app.AppViewFactory
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.pager.PagerViewModel

class PagerView : AndroidView<PagerViewModel>() {

    override val layoutResId: Int = R.layout.screen_pages

    override fun onBindView(view: View, viewModel: PagerViewModel) {
        with(view) {
            +pagerNavigatorBinding(viewPager, viewModel.pages, AppViewFactory)
            +(addTextStart sendClicksTo viewModel::addTextStart)
            +(addTextEnd sendClicksTo viewModel::addTextEnd)
            +(addImageStart sendClicksTo viewModel::addImageStart)
            +(addImageEnd sendClicksTo viewModel::addImageEnd)
        }
    }

}