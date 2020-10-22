package net.apptronic.core.android.viewmodel.listadapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.navigation.ViewBinderListAdapter
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.ListUpdateSpec
import net.apptronic.core.mvvm.viewmodel.navigation.ViewModelItem

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

    override fun onDataChanged(items: List<ViewModelItem>, changeInfo: Any?) {
        when (changeInfo) {
            is ListUpdateSpec.ItemAdded -> notifyItemInserted(changeInfo.index)
            is ListUpdateSpec.ItemRemoved -> notifyItemRemoved(changeInfo.index)
            is ListUpdateSpec.ItemMoved -> notifyItemMoved(changeInfo.fromIndex, changeInfo.toIndex)
            is ListUpdateSpec.RangeInserted -> notifyItemRangeInserted(
                changeInfo.range.first,
                changeInfo.range.last - changeInfo.range.first + 1
            )
            is ListUpdateSpec.RangeRemoved -> notifyItemRangeRemoved(
                changeInfo.range.first,
                changeInfo.range.last - changeInfo.range.first + 1
            )
            else -> notifyDataSetChanged()
        }
    }

    fun getItemAt(position: Int): IViewModel {
        return listAdapter.getItemAt(position).viewModel
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
            val item = listAdapter.getItemAt(position)
            if (item != holder.item) {
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
        holder.item = listAdapter.getItemAt(position)
        val binder = listAdapter.bindView(position, holder.itemView)
        binder.viewModelItem.setVisible(true)
        binder.viewModelItem.setFocused(true)
        holder.viewBinder = binder
    }

    private fun doUnbindViewHolder(holder: ViewModelHolder) {
        val binder = holder.viewBinder
        if (binder != null) {
            binder.viewModelItem.setFocused(false)
            binder.viewModelItem.setVisible(false)
            listAdapter.unbindView(binder)
            holder.item = null
            holder.viewBinder = null
        }
    }

    inner class ViewModelHolder(view: View) : RecyclerView.ViewHolder(view) {
        var item: ViewModelItem? = null
        var viewBinder: ViewBinder<*>? = null
    }

}