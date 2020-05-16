package net.apptronic.test.commons_sample_app.views

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.lazy_filtered_list.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.bindings.navigation.listNavigatorBinding
import net.apptronic.core.android.viewmodel.bindings.setVisibleGoneFrom
import net.apptronic.test.commons_sample_app.AppViewFactory
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.lazylistfiltering.LazyListFilterViewModel

class LazyListFilterView : AndroidView<LazyListFilterViewModel>() {

    override var layoutResId: Int? = R.layout.lazy_filtered_list

    override fun onBindView(view: View, viewModel: LazyListFilterViewModel) {
        with(view) {
            listNavigatorBinding(lazyFilteredItemsList, viewModel.listNavigator)
            lazyFilteredItemsList.layoutManager = LinearLayoutManager(context)
            +(progressBar setVisibleGoneFrom viewModel.isInProgress)
        }
    }

}