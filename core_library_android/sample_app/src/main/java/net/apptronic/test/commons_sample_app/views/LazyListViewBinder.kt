package net.apptronic.test.commons_sample_app.views

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.lazy_list.view.*
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.navigation.bindListNavigator
import net.apptronic.core.android.viewmodel.listadapters.BindingStrategy
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.lazylist.LazyListViewModel

class LazyListViewBinder : ViewBinder<LazyListViewModel>() {

    override var layoutResId: Int? = R.layout.lazy_list

    override fun onBindView(view: View, viewModel: LazyListViewModel) {
        with(view) {
            bindListNavigator(
                lazyList, viewModel.navigator,
                bindingStrategy = BindingStrategy.UntilReused
            )
            lazyList.layoutManager = LinearLayoutManager(context)
        }
    }

}