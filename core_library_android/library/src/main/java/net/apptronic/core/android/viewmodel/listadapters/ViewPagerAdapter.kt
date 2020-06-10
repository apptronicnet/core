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

    private val androidViews = mutableMapOf<Long, ViewBinder<*>>()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val viewModel = viewModelAdapter.getItemAt(position)
        val view = viewModelAdapter.createView(viewModel, container)
        val androidView = viewModelAdapter.bindView(viewModel, position, view)
        container.addView(view)
        androidViews[viewModel.id] = androidView
        return androidView
    }

    override fun destroyItem(collection: ViewGroup, position: Int, obj: Any) {
        val androidView = obj as ViewBinder<*>
        viewModelAdapter.unbindView(androidView)
        collection.removeView(androidView.getView())
        androidViews.remove(androidView.getViewModel().id)
    }

    override fun getCount(): Int {
        return viewModelAdapter.getSize()
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        val androidView = obj as ViewBinder<*>
        return view === androidView.getView()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val viewModel = viewModelAdapter.getItemAt(position)
        return titleProvider?.getItemTitle(viewModel, position) ?: ""
    }

    override fun getItemPosition(obj: Any): Int {
        val androidView = obj as ViewBinder<*>
        val index = viewModelAdapter.indexOf(androidView.getViewModel())
        return if (index >= 0) index else POSITION_NONE
    }

}