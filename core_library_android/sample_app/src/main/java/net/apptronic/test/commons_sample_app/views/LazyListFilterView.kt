package net.apptronic.test.commons_sample_app.views

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.lazy_filtered_list.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.bindings.bindVisibleGone
import net.apptronic.core.android.viewmodel.bindings.navigation.bindListNavigator
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.lazylistfiltering.LazyListFilterViewModel

class LazyListFilterView : AndroidView<LazyListFilterViewModel>() {

    override var layoutResId: Int? = R.layout.lazy_filtered_list

    override fun onBindView(view: View, viewModel: LazyListFilterViewModel) {
        with(view) {
            bindListNavigator(lazyFilteredItemsList, viewModel.listNavigator)
            lazyFilteredItemsList.layoutManager = LinearLayoutManager(context)
            bindVisibleGone(progressBar, viewModel.isInProgress)
        }
    }

}