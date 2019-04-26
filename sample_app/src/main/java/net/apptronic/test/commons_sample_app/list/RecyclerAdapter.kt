package net.apptronic.test.commons_sample_app.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.apptronic.core.android.viewmodel.AndroidViewModelListAdapter
import net.apptronic.core.mvvm.viewmodel.ViewModel

class RecyclerAdapter(
    private val controller: AndroidViewModelListAdapter
) : RecyclerView.Adapter<Holder>() {

    var items = emptyList<ViewModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int {
        return controller.getViewType(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(controller.createView(viewType, parent))
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onViewAttachedToWindow(holder: Holder) {
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: Holder) {
        super.onViewDetachedFromWindow(holder)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        controller.bindView(items[position], holder.itemView)
    }

}

class Holder(view: View) : RecyclerView.ViewHolder(view)