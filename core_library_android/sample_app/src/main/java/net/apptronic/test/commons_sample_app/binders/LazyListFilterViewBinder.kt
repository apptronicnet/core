package net.apptronic.test.commons_sample_app.binders

import androidx.recyclerview.widget.LinearLayoutManager
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindVisibleGone
import net.apptronic.core.android.viewmodel.bindings.navigation.bindNavigator
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.databinding.LazyFilteredListBinding
import net.apptronic.test.commons_sample_app.lazylistfiltering.LazyListFilterViewModel

class LazyListFilterViewBinder : ViewBinder<LazyListFilterViewModel>() {

    override var layoutResId: Int? = R.layout.lazy_filtered_list

    override fun onBindView() = withBinding(LazyFilteredListBinding::bind) {
        bindNavigator(lazyFilteredItemsList, viewModel.listNavigator)
        lazyFilteredItemsList.layoutManager = LinearLayoutManager(context)
        bindVisibleGone(progressBar, viewModel.isInProgress)
    }

}