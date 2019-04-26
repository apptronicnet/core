package net.apptronic.core.mvvm.viewmodel.adapter

import net.apptronic.core.mvvm.viewmodel.ViewModel

abstract class ViewModelListAdapter(
    private val viewController: ViewController
) {

    interface ViewController {

        fun onDataSetChanged(items: List<ViewModel>)

        fun onRequestedVisibleRange()

    }

    private var rangeChangedListener: RangeChangedListener? = null
    private var isRequestedForceUpdate = false
    private var start = 0
    private var end = 0

    fun notifyListRecreated() {
        updateVisibleRange(0, 0)
    }

    fun updateVisibleRange(start: Int, end: Int) {
        val changed = this.start != start || this.end != end
        if (changed || isRequestedForceUpdate) {
            this.start = start
            this.end = end
            rangeChangedListener?.onVisibleRangeChanged(start, end)
            isRequestedForceUpdate = false
        }
    }

    fun updateDataSet(items: List<ViewModel>) {
        viewController.onDataSetChanged(items)
        requestVisibleRange()
    }

    fun requestVisibleRange() {
        isRequestedForceUpdate = true
        viewController.onRequestedVisibleRange()
    }

    internal fun setRangeChangedListener(listener: RangeChangedListener?) {
        this.rangeChangedListener = listener
    }

}

internal interface RangeChangedListener {

    fun onVisibleRangeChanged(start: Int, end: Int)

}