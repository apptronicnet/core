package net.apptronic.core.android.viewmodel.listadapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.AndroidViewModelListAdapter
import net.apptronic.core.mvvm.viewmodel.ViewModel

class RecyclerViewAdapter(
    private val viewModelAdapter: AndroidViewModelListAdapter,
    private val bindingStrategy: BindingStrategy
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewModelHolder>() {

    init {
        setHasStableIds(true)
        viewModelAdapter.addListener {
            notifyDataSetChanged()
        }
    }

    fun getItemAt(position: Int): ViewModel {
        return viewModelAdapter.getItemAt(position)
    }

    override fun getItemId(position: Int): Long {
        return viewModelAdapter.getItemAt(position).id
    }

    override fun getItemCount(): Int {
        return viewModelAdapter.getSize()
    }

    override fun getItemViewType(position: Int): Int {
        return viewModelAdapter.getViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewModelHolder {
        return ViewModelHolder(viewModelAdapter.createView(viewType, parent))
    }

    override fun onBindViewHolder(holder: ViewModelHolder, position: Int) {
        val viewModel = viewModelAdapter.getItemAt(position)
        if (viewModel != holder.viewModel) {
            if (bindingStrategy == BindingStrategy.UntilReused) {
                unbindViewHolder(holder)
            }
            holder.viewModel = viewModel
            holder.androidView = viewModelAdapter.bindView(viewModel, position, holder.itemView)
        }
    }

    override fun onViewRecycled(holder: ViewModelHolder) {
        super.onViewRecycled(holder)
        if (bindingStrategy == BindingStrategy.MatchRecycle) {
            unbindViewHolder(holder)
        }
    }

    private fun unbindViewHolder(holder: ViewModelHolder) {
        val androidView = holder.androidView
        if (androidView != null) {
            viewModelAdapter.unbindView(androidView)
            holder.viewModel = null
            holder.androidView = null
        }
    }

    inner class ViewModelHolder(view: View) : RecyclerView.ViewHolder(view) {
        var viewModel: ViewModel? = null
        var androidView: AndroidView<*>? = null
    }

}