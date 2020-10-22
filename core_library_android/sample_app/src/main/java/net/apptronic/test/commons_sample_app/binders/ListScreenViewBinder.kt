package net.apptronic.test.commons_sample_app.binders

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindClickListener
import net.apptronic.core.android.viewmodel.bindings.bindText
import net.apptronic.core.android.viewmodel.bindings.navigation.bindNavigator
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.databinding.ScreenListBinding
import net.apptronic.test.commons_sample_app.list.ListScreenViewModel

class ListScreenViewBinder : ViewBinder<ListScreenViewModel>() {

    override var layoutResId: Int? = R.layout.screen_list

    override fun onBindView() = withBinging(ScreenListBinding::bind) {
        bindText(listTitle, viewModel.title)

        bindNavigator(listOfItems, viewModel.listNavigator)
        listOfItems.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        bindClickListener(addTextTop, viewModel::onClickAddTextToStart)
        bindClickListener(addTextMiddle, viewModel::onClickAddTextToMiddle)
        bindClickListener(addTextBottom, viewModel::onClickAddTextToEnd)

        bindClickListener(addImageTop, viewModel::onClickAddImageToStart)
        bindClickListener(addImageMiddle, viewModel::onClickAddImageToMiddle)
        bindClickListener(addImageBottom, viewModel::onClickAddImageToEnd)
    }

}