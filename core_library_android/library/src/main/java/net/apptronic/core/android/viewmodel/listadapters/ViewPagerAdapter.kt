package net.apptronic.core.android.viewmodel.listadapters

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.navigation.ViewBinderListAdapter

class ViewPagerAdapter(
    private val viewModelAdapter: ViewBinderListAdapter
) : PagerAdapter() {

    var titleProvider: TitleProvider? = null

    init {
        viewModelAdapter.addListener {
            notifyDataSetChanged()
        }
    }

    private val viewBinders = mutableMapOf<Long, ViewBinder<*>>()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val viewModel = viewModelAdapter.getItemAt(position)
        val view = viewModelAdapter.createView(viewModel, container)
        val binder = viewModelAdapter.bindView(viewModel, position, view)
        container.addView(view)
        viewBinders[viewModel.componentId] = binder
        return binder
    }

    override fun destroyItem(collection: ViewGroup, position: Int, obj: Any) {
        val binder = obj as ViewBinder<*>
        viewModelAdapter.unbindView(binder)
        collection.removeView(binder.getView())
        viewBinders.remove(binder.getViewModel().componentId)
    }

    override fun getCount(): Int {
        return viewModelAdapter.getSize()
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        val binder = obj as ViewBinder<*>
        return view === binder.getView()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val viewModel = viewModelAdapter.getItemAt(position)
        return titleProvider?.getItemTitle(viewModel, position) ?: ""
    }

    override fun getItemPosition(obj: Any): Int {
        val binder = obj as ViewBinder<*>
        val index = viewModelAdapter.indexOf(binder.getViewModel())
        return if (index >= 0) index else POSITION_NONE
    }

}