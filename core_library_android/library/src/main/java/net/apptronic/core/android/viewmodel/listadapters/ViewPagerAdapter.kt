package net.apptronic.core.android.viewmodel.listadapters

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.navigation.ViewBinderListAdapter
import net.apptronic.core.mvvm.viewmodel.ViewModel

class ViewPagerAdapter(
    private val binderAdapter: ViewBinderListAdapter
) : PagerAdapter(), ViewBinderListAdapter.UpdateListener {

    var titleProvider: TitleProvider? = null

    init {
        binderAdapter.addListener(this)
    }

    override fun onDataChanged(items: List<ViewModel>, changeInfo: Any?) {
        notifyDataSetChanged()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val viewModel = binderAdapter.getViewModelAt(position)
        val view = binderAdapter.createView(viewModel, container)
        val binder = binderAdapter.bindView(position, view)
        container.addView(view)
        return binder
    }

    override fun destroyItem(collection: ViewGroup, position: Int, obj: Any) {
        val binder = obj as ViewBinder<*>
        binderAdapter.unbindView(binder)
        collection.removeView(binder.getView())
    }

    override fun getCount(): Int {
        return binderAdapter.getSize()
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        val binder = obj as ViewBinder<*>
        return view === binder.getView()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val viewModel = binderAdapter.getViewModelAt(position)
        return titleProvider?.getItemTitle(viewModel, position) ?: ""
    }

    override fun getItemPosition(obj: Any): Int {
        val binder = obj as ViewBinder<*>
        val index = binderAdapter.indexOf(binder)
        return if (index >= 0) index else POSITION_NONE
    }

}