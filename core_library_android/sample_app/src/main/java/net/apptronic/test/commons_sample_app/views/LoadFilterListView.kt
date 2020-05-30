package net.apptronic.test.commons_sample_app.views

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.load_filter_list.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.bindings.navigation.bindListNavigator
import net.apptronic.core.component.entity.base.UpdateEntity
import net.apptronic.core.component.entity.subscribe
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.loadfilterlist.LoadFilterListViewModel
import net.apptronic.test.commons_sample_app.loadfilterlist.LoadFilterMode

class LoadFilterListView : AndroidView<LoadFilterListViewModel>() {

    override var layoutResId: Int? = R.layout.load_filter_list

    override fun onBindView(view: View, viewModel: LoadFilterListViewModel) {
        with(view) {
            bindListNavigator(loadFilterList, viewModel.list)
            loadFilterList.layoutManager = LinearLayoutManager(context)
            LoadFilterMode.values().forEachIndexed { index, mode ->
                val textView = modes.getChildAt(index) as TextView
                bindMode(textView, viewModel.loadFilterMode, mode)
            }
        }
    }

    private fun bindMode(
        textView: TextView,
        entity: UpdateEntity<LoadFilterMode>,
        mode: LoadFilterMode
    ) {
        textView.text = mode.title
        textView.setOnClickListener {
            entity.update(mode)
        }
        val selectedColor = ContextCompat.getColor(textView.context, R.color.selection)
        entity.subscribe {
            val selected = it == mode
            textView.background = if (selected) ColorDrawable(selectedColor) else null
        }
    }

}