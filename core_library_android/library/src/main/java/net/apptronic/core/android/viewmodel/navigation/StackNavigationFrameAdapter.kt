package net.apptronic.core.android.viewmodel.navigation

import android.view.View
import android.view.ViewGroup
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.transitions.TransitionBuilder
import net.apptronic.core.android.viewmodel.transitions.ViewSwitch
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.BackNavigationStatus
import net.apptronic.core.mvvm.viewmodel.navigation.HasBackNavigation
import net.apptronic.core.mvvm.viewmodel.navigation.StackNavigationViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.TransitionInfo

class StackNavigationFrameAdapter(
    private val container: ViewGroup,
    private val transitionBuilder: TransitionBuilder = TransitionBuilder(),
    private val defaultAnimationTime: Long =
        container.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong(),
    private val maxSavedViews: Int = 5,
    private val listAdapter: ViewBinderListAdapter
) : ViewBinderListAdapter.UpdateListener {

    private var isInTransition = false
    private var items: List<ViewModel> = emptyList()

    private val viewBinders = mutableListOf<AttachedBinder>()

    init {
        container.removeAllViews()
        listAdapter.addListener(this)
    }

    fun bind(model: StackNavigationViewModel) {
        model.listNavigator.setAdapter(listAdapter)
    }

    override fun onDataChanged(items: List<ViewModel>, changeInfo: Any?) {
        this.items = items
        val transitionInfo = changeInfo as? TransitionInfo ?: TransitionInfo(true, null)
        invalidateState(transitionInfo)
    }

    private var currentBinder: AttachedBinder? = null

    private fun clearViews() {
        val views = (0 until container.childCount).map {
            container.getChildAt(it)
        }
        views.forEach { view ->
            if (viewBinders.any {
                    it.view == view
                }.not()) {
                container.removeView(view)
            }
        }
    }

    private fun invalidateState(transitionInfo: TransitionInfo) {
        clearViews()
        val currentViewModel = if (listAdapter.getSize() > 0) {
            listAdapter.getViewModelAt(listAdapter.getSize() - 1)
        } else {
            null
        }
        val previousBinder = currentBinder
        if (currentViewModel != null) {
            val viewBinder = getOrCreateBinder(currentViewModel)
            currentBinder = viewBinder
        } else {
            currentBinder = null
        }
        val currentBinder = this.currentBinder
        viewBinders.toTypedArray().forEach {
            val actual = listAdapter.contains(it.viewModel)
            if (!actual) {
                detachBinder(it, it != previousBinder)
            }
        }
        removeSavedBindersIfNeeded()
        if (currentBinder != previousBinder) {
            if (currentBinder != null) {
                listAdapter.setVisible(currentBinder.binder, true)
                listAdapter.setFocused(currentBinder.binder, true)
            }
            if (previousBinder != null) {
                listAdapter.setFocused(previousBinder.binder, false)
                listAdapter.setVisible(previousBinder.binder, false)
            }

            val viewSwitch = ViewSwitch(
                entering = currentBinder?.view,
                exiting = previousBinder?.view,
                container = container,
                isNewOnFront = transitionInfo.isNewOnFront
            )
            val transition = transitionBuilder.getViewSwitchTransition(
                viewSwitch,
                transitionInfo.spec,
                defaultAnimationTime
            )
            transition.doOnStart {
                currentBinder?.view?.visibility = View.VISIBLE
                isInTransition = true
            }.doOnCompleteOrCancel {
                isInTransition = false
                previousBinder?.let {
                    binderHidden(it)
                }
            }.launch(viewSwitch)
        }
    }

    private fun binderHidden(binder: AttachedBinder) {
        if (viewBinders.contains(binder)) {
            binder.view.visibility = View.GONE
        } else {
            container.removeView(binder.view)
        }
    }

    private fun removeSavedBindersIfNeeded() {
        while (needRemoveSavedBinders()) {
            val toRemove = viewBinders.firstOrNull {
                it != currentBinder
            }
            if (toRemove != null) {
                detachBinder(toRemove, true)
            } else {
                return
            }
        }
    }

    private fun needRemoveSavedBinders(): Boolean {
        return viewBinders.filter { it != currentBinder }.size > maxSavedViews
    }

    fun getViewAt(position: Int): View {
        return getOrCreateBinder(listAdapter.getViewModelAt(position)).view
    }

    fun unbind() {
        viewBinders.toTypedArray().forEach {
            detachBinder(it, false)
        }
    }

    private fun getOrCreateBinder(viewModel: ViewModel): AttachedBinder {
        return viewBinders.firstOrNull {
            it.viewModel == viewModel
        } ?: attachBinder(viewModel)
    }

    private fun attachBinder(viewModel: ViewModel): AttachedBinder {
        val view = listAdapter.createView(viewModel, container)
        val viewBinder = listAdapter.bindView(viewModel, view)
        view.visibility = View.GONE
        val attachedBinder = AttachedBinder(viewBinder)
        viewBinders.add(attachedBinder)
        sortBinders()
        return attachedBinder
    }

    private inner class AttachedBinder(
        val binder: ViewBinder<*>
    ) : Comparable<AttachedBinder> {
        var position = 0
        val view = binder.getView()
        val viewModel = binder.getViewModel()
        fun refreshPosition(items: List<ViewModel>) {
            val index = items.indexOf(viewModel)
            if (index >= 0) {
                position = index
            }
        }

        override fun compareTo(other: AttachedBinder): Int {
            return position.compareTo(other.position)
        }
    }

    private fun sortBinders() {
        viewBinders.forEach {
            it.refreshPosition(items)
            container.removeAllViews()
        }
        viewBinders.sort()
        viewBinders.forEach {
            container.addView(it.view)
        }
    }

    private fun detachBinder(binder: AttachedBinder, removeView: Boolean) {
        if (removeView) {
            container.removeView(binder.view)
        }
        listAdapter.unbindView(binder.binder)
        viewBinders.remove(binder)
    }

    fun onConfirmBackNavigationFromGesture() {
        val hasBackNavigation = currentBinder?.viewModel as? HasBackNavigation
        hasBackNavigation?.onBackNavigationConfirmedEvent()
    }

    fun onRestrictedBackNavigationFromGesture() {
        val hasBackNavigation = currentBinder?.viewModel as? HasBackNavigation
        hasBackNavigation?.onBackNavigationRestrictedEvent()
    }

    fun getBackNavigationStatus(): BackNavigationStatus {
        if (isInTransition) {
            return BackNavigationStatus.Disabled
        }
        val hasBackNavigation = currentBinder?.viewModel as? HasBackNavigation
        return hasBackNavigation?.getBackNavigationStatus() ?: BackNavigationStatus.Disabled
    }

}