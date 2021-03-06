package net.apptronic.test.commons_sample_app.binders

import android.graphics.drawable.ColorDrawable
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.navigation.bindNavigator
import net.apptronic.core.entity.base.SubjectEntity
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.databinding.LoadFilterListBinding
import net.apptronic.test.commons_sample_app.loadfilterlist.LoadFilterListViewModel
import net.apptronic.test.commons_sample_app.loadfilterlist.LoadFilterMode

class LoadFilterListViewBinder : ViewBinder<LoadFilterListViewModel>() {

    override var layoutResId: Int? = R.layout.load_filter_list

    override fun onBindView() = withBinding(LoadFilterListBinding::bind) {
        bindNavigator(loadFilterList, viewModel.list)
        loadFilterList.layoutManager = LinearLayoutManager(context)
        LoadFilterMode.values().forEachIndexed { index, mode ->
            val textView = modes.getChildAt(index) as TextView
            bindMode(textView, viewModel.loadFilterMode, mode)
        }
    }

    private fun bindMode(
        textView: TextView,
        entity: SubjectEntity<LoadFilterMode>,
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