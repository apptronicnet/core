package net.apptronic.core.android.viewmodel.listadapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.navigation.ViewBinderListAdapter
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.*

private const val NO_TYPE = -1
private const val NO_ID = -1L

class RecyclerViewAdapter(
    private val listAdapter: ViewBinderListAdapter,
    private val bindingStrategy: BindingStrategy
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewModelHolder>(),
    ViewBinderListAdapter.UpdateListener {

    private var isBound = true

    fun onUnbound() {
        isBound = false
    }

    init {
        setHasStableIds(true)
        listAdapter.addListener(this)
    }

    override fun onDataChanged(items: List<ViewModel>, changeInfo: Any?) {
        when (changeInfo) {
            is ItemAdded -> notifyItemInserted(changeInfo.index)
            is ItemRemoved -> notifyItemRemoved(changeInfo.index)
            is ItemMoved -> notifyItemMoved(changeInfo.fromIndex, changeInfo.toIndex)
            is RangeInserted -> notifyItemRangeInserted(
                changeInfo.range.first,
                changeInfo.range.endInclusive - changeInfo.range.first + 1
            )
            is RangeRemoved -> notifyItemRangeRemoved(
                changeInfo.range.first,
                changeInfo.range.endInclusive - changeInfo.range.first + 1
            )
            else -> notifyDataSetChanged()
        }
    }

    fun getItemAt(position: Int): ViewModel {
        return listAdapter.getViewModelAt(position)
    }

    override fun getItemId(position: Int): Long {
        return if (isBound) {
            listAdapter.getId(position)
        } else NO_ID
    }

    override fun getItemCount(): Int {
        return listAdapter.getSize()
    }

    override fun getItemViewType(position: Int): Int {
        return if (isBound) {
            listAdapter.getViewType(position)
        } else NO_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewModelHolder {
        return if (isBound) {
            ViewModelHolder(listAdapter.createView(viewType, parent))
        } else ViewModelHolder(View(parent.context))
    }

    override fun onBindViewHolder(holder: ViewModelHolder, position: Int) {
        if (isBound) {
            val viewModel = listAdapter.getViewModelAt(position)
            if (viewModel != holder.viewModel) {
                doUnbindViewHolder(holder)
                doBindViewHolder(holder, position)
            }
        }
    }

    override fun onViewRecycled(holder: ViewModelHolder) {
        super.onViewRecycled(holder)
        if (bindingStrategy == BindingStrategy.MatchRecycle) {
            doUnbindViewHolder(holder)
        }
    }

    private fun doBindViewHolder(holder: ViewModelHolder, position: Int) {
        holder.viewModel = listAdapter.getViewModelAt(position)
        val binder = listAdapter.bindView(position, holder.itemView)
        listAdapter.setVisible(binder, true)
        listAdapter.setFocused(binder, true)
        holder.viewBinder = binder
    }

    private fun doUnbindViewHolder(holder: ViewModelHolder) {
        val binder = holder.viewBinder
        if (binder != null) {
            listAdapter.setFocused(binder, false)
            listAdapter.setVisible(binder, false)
            listAdapter.unbindView(binder)
            holder.viewModel = null
            holder.viewBinder = null
        }
    }

    inner class ViewModelHolder(view: View) : RecyclerView.ViewHolder(view) {
        var viewModel: ViewModel? = null
        var viewBinder: ViewBinder<*>? = null
    }

}