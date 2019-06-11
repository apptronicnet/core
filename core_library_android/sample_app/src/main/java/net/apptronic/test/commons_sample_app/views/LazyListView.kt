package net.apptronic.test.commons_sample_app.views

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.lazy_list.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.bindings.navigation.listNavigatorBinding
import net.apptronic.test.commons_sample_app.AppViewFactory
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.lazylist.LazyListViewModel

class LazyListView : AndroidView<LazyListViewModel>() {

    override var layoutResId: Int? = R.layout.lazy_list

    override fun onBindView(view: View, viewModel: LazyListViewModel) {
        with(view) {
            +listNavigatorBinding(lazyList, viewModel.items, AppViewFactory)
            lazyList.layoutManager = LinearLayoutManager(context)
        }
    }

}