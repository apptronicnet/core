package net.apptronic.core.android.viewmodel.listadapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.navigation.ViewBinderListAdapter
import net.apptronic.core.mvvm.viewmodel.ViewModel

class RecyclerViewAdapter(
    private val viewModelAdapter: ViewBinderListAdapter,
    private val bindingStrategy: BindingStrategy
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewModelHolder>() {

    private var isBound = true

    private val NO_TYPE = -1
    private val NO_ID = -1L

    fun onUnbound() {
        isBound = false
    }

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
        return if (isBound) {
            viewModelAdapter.getItemAt(position).id
        } else NO_ID
    }

    override fun getItemCount(): Int {
        return viewModelAdapter.getSize()
    }

    override fun getItemViewType(position: Int): Int {
        return if (isBound) {
            viewModelAdapter.getViewType(position)
        } else NO_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewModelHolder {
        return if (isBound) {
            ViewModelHolder(viewModelAdapter.createView(viewType, parent))
        } else ViewModelHolder(View(parent.context))
    }

    override fun onBindViewHolder(holder: ViewModelHolder, position: Int) {
        if (isBound) {
            val viewModel = viewModelAdapter.getItemAt(position)
            if (viewModel != holder.viewModel) {
                if (bindingStrategy == BindingStrategy.UntilReused) {
                    unbindViewHolder(holder)
                }
                holder.viewModel = viewModel
                holder.viewBinder = viewModelAdapter.bindView(viewModel, position, holder.itemView)
            }
        }
    }

    override fun onViewRecycled(holder: ViewModelHolder) {
        super.onViewRecycled(holder)
        if (bindingStrategy == BindingStrategy.MatchRecycle) {
            unbindViewHolder(holder)
        }
    }

    private fun unbindViewHolder(holder: ViewModelHolder) {
        val androidView = holder.viewBinder
        if (androidView != null) {
            viewModelAdapter.unbindView(androidView)
            holder.viewModel = null
            holder.viewBinder = null
        }
    }

    inner class ViewModelHolder(view: View) : RecyclerView.ViewHolder(view) {
        var viewModel: ViewModel? = null
        var viewBinder: ViewBinder<*>? = null
    }

}