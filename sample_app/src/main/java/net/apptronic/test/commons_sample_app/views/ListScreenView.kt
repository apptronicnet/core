package net.apptronic.test.commons_sample_app.views

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.screen_list.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.bindings.navigation.listNavigatorBinding
import net.apptronic.core.android.viewmodel.bindings.sendClicksTo
import net.apptronic.core.android.viewmodel.bindings.setTextFrom
import net.apptronic.test.commons_sample_app.AppViewFactory
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.list.ListScreenViewModel

class ListScreenView : AndroidView<ListScreenViewModel>() {

    override val layoutResId: Int = R.layout.screen_list

    override fun onBindView(view: View, viewModel: ListScreenViewModel) {
        with(view) {
            +(listTitle setTextFrom viewModel.title)

            +listNavigatorBinding(listOfItems, viewModel.listNavigator, AppViewFactory)
            listOfItems.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

            +(addTextTop sendClicksTo viewModel::onClickAddTextToStart)
            +(addTextMiddle sendClicksTo viewModel::onClickAddTextToMiddle)
            +(addTextBottom sendClicksTo viewModel::onClickAddTextToEnd)

            +(addImageTop sendClicksTo viewModel::onClickAddImageToStart)
            +(addImageMiddle sendClicksTo viewModel::onClickAddImageToMiddle)
            +(addImageBottom sendClicksTo viewModel::onClickAddImageToEnd)
        }
    }

}