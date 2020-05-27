package net.apptronic.test.commons_sample_app.views

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.screen_list.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.bindings.bindClickListener
import net.apptronic.core.android.viewmodel.bindings.bindText
import net.apptronic.core.android.viewmodel.bindings.navigation.bindListNavigator
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.list.ListScreenViewModel

class ListScreenView : AndroidView<ListScreenViewModel>() {

    override var layoutResId: Int? = R.layout.screen_list

    override fun onBindView(view: View, viewModel: ListScreenViewModel) {
        with(view) {
            bindText(listTitle, viewModel.title)

            bindListNavigator(listOfItems, viewModel.listNavigator)
            listOfItems.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

            bindClickListener(addTextTop, viewModel::onClickAddTextToStart)
            bindClickListener(addTextMiddle, viewModel::onClickAddTextToMiddle)
            bindClickListener(addTextBottom, viewModel::onClickAddTextToEnd)

            bindClickListener(addImageTop, viewModel::onClickAddImageToStart)
            bindClickListener(addImageMiddle, viewModel::onClickAddImageToMiddle)
            bindClickListener(addImageBottom, viewModel::onClickAddImageToEnd)
        }
    }

}