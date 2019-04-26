package net.apptronic.core.android.viewmodel.listadapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.AndroidViewModelListAdapter

class RecyclerViewAdapter(
    private val viewModelAdapter: AndroidViewModelListAdapter
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewModelHolder>() {

    init {
        viewModelAdapter.addListener {
            notifyDataSetChanged()
        }
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
        holder.androidView?.let {
            viewModelAdapter.unbindView(it)
        }
        holder.androidView = viewModelAdapter.bindView(viewModel, holder.itemView)
    }

    override fun onViewRecycled(holder: ViewModelHolder) {
        super.onViewRecycled(holder)
        holder.androidView?.let {
            viewModelAdapter.unbindView(it)
        }
        holder.androidView = null
    }

    inner class ViewModelHolder(view: View) : RecyclerView.ViewHolder(view) {
        var androidView: AndroidView<*>? = null
    }

}