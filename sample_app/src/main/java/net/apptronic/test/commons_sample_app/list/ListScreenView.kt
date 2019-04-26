package net.apptronic.test.commons_sample_app.list

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.screen_list.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.AndroidViewModelListAdapter
import net.apptronic.core.android.viewmodel.bindings.sendClicksTo
import net.apptronic.core.android.viewmodel.bindings.setTextFrom
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter
import net.apptronic.test.commons_sample_app.MainModelFactory
import net.apptronic.test.commons_sample_app.R

class ListScreenView : AndroidView<ListScreenViewModel>() {

    init {
        layoutResId = R.layout.screen_list
    }

    override fun onBindView(view: View, viewModel: ListScreenViewModel) {
        with(view) {
            +(listTitle setTextFrom viewModel.title)

            val controller = ViewControllerImpl(listOfItems)
            val viewModelAdapter = AndroidViewModelListAdapter(controller, MainModelFactory)
            controller.listAdapter = RecyclerAdapter(viewModelAdapter)
            controller.viewModelAdapter = viewModelAdapter
            listOfItems.adapter = controller.listAdapter
            viewModel.listNavigator.setAdapter(viewModelAdapter)

            +(addTextTop sendClicksTo viewModel::onClickAddTextToStart)
            +(addTextMiddle sendClicksTo viewModel::onClickAddTextToMiddle)
            +(addTextBottom sendClicksTo viewModel::onClickAddTextToEnd)

            +(addImageTop sendClicksTo viewModel::onClickAddImageToStart)
            +(addImageMiddle sendClicksTo viewModel::onClickAddImageToMiddle)
            +(addImageBottom sendClicksTo viewModel::onClickAddImageToEnd)
        }
    }

    class ViewControllerImpl(
        val list: RecyclerView
    ) : RecyclerView.OnScrollListener(), ViewModelListAdapter.ViewController {

        lateinit var listAdapter: RecyclerAdapter
        lateinit var viewModelAdapter: AndroidViewModelListAdapter

        private val layoutManger = LinearLayoutManager(list.context, RecyclerView.VERTICAL, false)

        init {
            list.layoutManager = layoutManger
            list.addOnScrollListener(this)
        }

        override fun onDataSetChanged(items: List<ViewModel>) {
            listAdapter.items = items
            viewModelAdapter.notifyListRecreated()
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            onRequestedVisibleRange()
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            onRequestedVisibleRange()
        }

        override fun onRequestedVisibleRange() {
            val start = layoutManger.findFirstVisibleItemPosition()
            val end = layoutManger.findLastVisibleItemPosition()
            viewModelAdapter.updateVisibleRange(start, end)
        }

    }

}