package net.apptronic.test.commons_sample_app.binders

import androidx.recyclerview.widget.LinearLayoutManager
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.navigation.ViewListMode
import net.apptronic.core.android.viewmodel.bindings.navigation.bindNavigator
import net.apptronic.core.android.viewmodel.listadapters.BindingStrategy
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.databinding.LazyListBinding
import net.apptronic.test.commons_sample_app.lazylist.LazyListViewModel

class LazyListViewBinder : ViewBinder<LazyListViewModel>() {

    override var layoutResId: Int? = R.layout.lazy_list

    override fun onBindView() = withBinding(LazyListBinding::bind) {
        bindNavigator(
            lazyList, viewModel.navigator,
            ViewListMode(bindingStrategy = BindingStrategy.UntilReused)
        )
        lazyList.layoutManager = LinearLayoutManager(context)
    }

}