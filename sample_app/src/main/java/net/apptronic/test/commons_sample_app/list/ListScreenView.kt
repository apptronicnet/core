package net.apptronic.test.commons_sample_app.list

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.screen_list.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.AndroidViewModelListAdapter
import net.apptronic.core.android.viewmodel.ViewModelBinding
import net.apptronic.core.android.viewmodel.bindings.ClickActionBinding
import net.apptronic.core.android.viewmodel.bindings.TextBinding
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter
import net.apptronic.test.commons_sample_app.MainModelFactory
import net.apptronic.test.commons_sample_app.R

class ListScreenView : AndroidView<ListScreenViewModel>() {

    init {
        layoutResId = R.layout.screen_list
    }

    override fun onCreateBinding(
        view: View,
        viewModel: ListScreenViewModel
    ): ViewModelBinding<ListScreenViewModel> {
        return createBinding(view, viewModel) {
            with(view) {
                listTitle.bindTo(TextBinding(), viewModel.title)

                val controller = ViewControllerImpl(listOfItems)
                val viewModelAdapter = AndroidViewModelListAdapter(controller, MainModelFactory)
                controller.listAdapter = RecyclerAdapter(viewModelAdapter)
                controller.viewModelAdapter = viewModelAdapter
                listOfItems.adapter = controller.listAdapter
                viewModel.listNavigator.setAdapter(viewModelAdapter)

                addTextTop.bindTo(ClickActionBinding(), viewModel::onClickAddTextToStart)
                addTextMiddle.bindTo(ClickActionBinding(), viewModel::onClickAddTextToMiddle)
                addTextBottom.bindTo(ClickActionBinding(), viewModel::onClickAddTextToEnd)

                addImageTop.bindTo(ClickActionBinding(), viewModel::onClickAddImageToStart)
                addImageMiddle.bindTo(ClickActionBinding(), viewModel::onClickAddImageToMiddle)
                addImageBottom.bindTo(ClickActionBinding(), viewModel::onClickAddImageToEnd)

            }
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