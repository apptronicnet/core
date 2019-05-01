package net.apptronic.test.commons_sample_app.pager

import android.view.View
import kotlinx.android.synthetic.main.screen_pages.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.AndroidViewModelListAdapter
import net.apptronic.core.android.viewmodel.bindings.sendClicksTo
import net.apptronic.core.android.viewmodel.listadapters.ViewPagerAdapter
import net.apptronic.test.commons_sample_app.MainModelFactory
import net.apptronic.test.commons_sample_app.R

class PagerView : AndroidView<PagerViewModel>() {

    init {
        layoutResId = R.layout.screen_pages
    }

    override fun onBindView(view: View, viewModel: PagerViewModel) {
        with(view) {
            val viewModelAdapter = AndroidViewModelListAdapter(MainModelFactory)
            viewPager.adapter = ViewPagerAdapter(viewModelAdapter)
            viewModel.pages.setAdapter(viewModelAdapter)
            +(addTextStart sendClicksTo viewModel::addTextStart)
            +(addTextEnd sendClicksTo viewModel::addTextEnd)
            +(addImageStart sendClicksTo viewModel::addImageStart)
            +(addImageEnd sendClicksTo viewModel::addImageEnd)
        }
    }

}