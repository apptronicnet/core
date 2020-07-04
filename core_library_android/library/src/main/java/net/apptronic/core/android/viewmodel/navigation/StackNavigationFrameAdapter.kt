package net.apptronic.core.android.viewmodel.navigation

import android.view.View
import android.view.ViewGroup
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.android.viewmodel.transitions.TransitionBuilder
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter
import net.apptronic.core.mvvm.viewmodel.navigation.BackNavigationStatus
import net.apptronic.core.mvvm.viewmodel.navigation.HasBackNavigation
import net.apptronic.core.mvvm.viewmodel.navigation.StackNavigationViewModel

class StackNavigationFrameAdapter(
    private val viewBinderFactory: ViewBinderFactory = ViewBinderFactory(),
    private val container: ViewGroup,
    private val transitionBuilder: TransitionBuilder = TransitionBuilder(),
    private val defaultAnimationTime: Long =
        container.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong(),
    private val maxSavedViews: Int = 5,
    private val navigatorAccess: NavigatorAccess
) : ViewModelListAdapter() {

    private var isInTransition = false

    interface NavigatorAccess {

        fun getTransition(from: ViewModel?, to: ViewModel?): Any?

    }

    private val viewBinders = mutableListOf<AttachedBinder>()

    init {
        container.removeAllViews()
        addListener {
            invalidateState()
        }
    }

    fun bind(model: StackNavigationViewModel) {
        model.listNavigator.setAdapter(this)
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

    private fun invalidateState() {
        clearViews()
        val currentViewModel = if (getSize() > 0) {
            getItemAt(getSize() - 1)
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
            val actual = getItems().contains(it.viewModel)
            if (!actual) {
                detachBinder(it, it != previousBinder)
            }
        }
        removeSavedBindersIfNeeded()
        if (currentBinder != previousBinder) {
            if (currentBinder != null) {
                setVisible(currentBinder.viewModel, true)
                setFocused(currentBinder.viewModel, true)
            }
            if (previousBinder != null) {
                setFocused(previousBinder.viewModel, false)
                setVisible(previousBinder.viewModel, false)
            }

            val transition = navigatorAccess.getTransition(
                previousBinder?.viewModel,
                currentBinder?.viewModel
            )

            if (currentBinder != null) {
                transitionBuilder.getEnterTransition(
                    container, currentBinder.view, transition, defaultAnimationTime
                ).doOnStart {
                    currentBinder.view.visibility = View.VISIBLE
                }.launch(currentBinder.view)
            }
            if (previousBinder != null) {
                transitionBuilder.getExitTransition(
                    container, previousBinder.view, transition, defaultAnimationTime
                ).doOnStart {
                    isInTransition = true
                }.doOnCompleteOrCancel {
                    isInTransition = false
                    binderHidden(previousBinder)
                }.launch(previousBinder.view)
            }
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
        return getOrCreateBinder(getItemAt(position)).view
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
        val viewBinder = viewBinderFactory.getBinder(viewModel)
        setBound(viewModel, true)
        val view = viewBinder.onCreateView(container)
        view.visibility = View.GONE
        viewBinder.bindView(view, viewModel)
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
        val items = getItems()
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
        setBound(binder.viewModel, false)
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