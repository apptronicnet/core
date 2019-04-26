package net.apptronic.test.commons_sample_app.list

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.screen_list.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.AndroidViewModelListAdapter
import net.apptronic.core.android.viewmodel.bindings.sendClicksTo
import net.apptronic.core.android.viewmodel.bindings.setTextFrom
import net.apptronic.core.android.viewmodel.listadapters.RecyclerViewAdapter
import net.apptronic.test.commons_sample_app.MainModelFactory
import net.apptronic.test.commons_sample_app.R

class ListScreenView : AndroidView<ListScreenViewModel>() {

    init {
        layoutResId = R.layout.screen_list
    }

    override fun onBindView(view: View, viewModel: ListScreenViewModel) {
        with(view) {
            +(listTitle setTextFrom viewModel.title)

            val viewModelAdapter = AndroidViewModelListAdapter(MainModelFactory)
            listOfItems.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            listOfItems.adapter = RecyclerViewAdapter(viewModelAdapter)
            viewModel.listNavigator.setAdapter(viewModelAdapter)

            +(addTextTop sendClicksTo viewModel::onClickAddTextToStart)
            +(addTextMiddle sendClicksTo viewModel::onClickAddTextToMiddle)
            +(addTextBottom sendClicksTo viewModel::onClickAddTextToEnd)

            +(addImageTop sendClicksTo viewModel::onClickAddImageToStart)
            +(addImageMiddle sendClicksTo viewModel::onClickAddImageToMiddle)
            +(addImageBottom sendClicksTo viewModel::onClickAddImageToEnd)
        }
    }

}