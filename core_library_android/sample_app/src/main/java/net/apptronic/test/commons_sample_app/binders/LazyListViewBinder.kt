package net.apptronic.test.commons_sample_app.binders

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.lazy_list.view.*
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.navigation.ViewListMode
import net.apptronic.core.android.viewmodel.bindings.navigation.bindNavigator
import net.apptronic.core.android.viewmodel.listadapters.BindingStrategy
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.lazylist.LazyListViewModel

class LazyListViewBinder : ViewBinder<LazyListViewModel>() {

    override var layoutResId: Int? = R.layout.lazy_list

    override fun onBindView(view: View, viewModel: LazyListViewModel) {
        with(view) {
            bindNavigator(
                lazyList, viewModel.navigator,
                ViewListMode(bindingStrategy = BindingStrategy.UntilReused)
            )
            lazyList.layoutManager = LinearLayoutManager(context)
        }
    }

}