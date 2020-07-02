package net.apptronic.core.android.viewmodel.navigation

import android.view.View
import android.view.ViewGroup
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.android.viewmodel.transitions.TransitionBuilder
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter
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

    interface NavigatorAccess {

        fun getTransition(from: ViewModel?, to: ViewModel?): Any?

    }

    private val viewBinders = mutableListOf<ViewBinder<*>>()

    init {
        container.removeAllViews()
        addListener {
            invalidateState()
        }
    }

    fun bind(model: StackNavigationViewModel) {
        model.listNavigator.setAdapter(this)
    }

    private var currentBinder: ViewBinder<*>? = null

    private fun clearViews() {
        val views = (0 until container.childCount).map {
            container.getChildAt(it)
        }
        views.forEach { view ->
            if (viewBinders.any {
                    it.getView() == view
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
            val actual = getItems().contains(it.getViewModel())
            if (!actual) {
                detachBinder(it, it != previousBinder)
            }
        }
        removeSavedBindersIfNeeded()
        if (currentBinder != previousBinder) {
            if (currentBinder != null) {
                currentBinder.getView().visibility = View.VISIBLE
                setVisible(currentBinder.getViewModel(), true)
                setFocused(currentBinder.getViewModel(), true)
            }
            if (previousBinder != null) {
                setFocused(previousBinder.getViewModel(), false)
                setVisible(previousBinder.getViewModel(), false)
            }

            val transition = navigatorAccess.getTransition(
                previousBinder?.getViewModel(),
                currentBinder?.getViewModel()
            )

            if (currentBinder != null) {
                transitionBuilder.getEnterTransition(
                    container, currentBinder.getView(), transition, defaultAnimationTime
                ).start(currentBinder.getView())
            }
            if (previousBinder != null) {
                transitionBuilder.getExitTransition(
                    container, previousBinder.getView(), transition, defaultAnimationTime
                ).doOnComplete {
                    binderHidden(previousBinder)
                }.doOnCancel {
                    binderHidden(previousBinder)
                }.start(previousBinder.getView())
            }
        }
    }

    private fun binderHidden(viewBinder: ViewBinder<*>) {
        if (viewBinders.contains(viewBinder)) {
            viewBinder.getView().visibility = View.GONE
        } else {
            container.removeView(viewBinder.getView())
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
        return getOrCreateBinder(getItemAt(position)).getView()
    }

    fun unbind() {
        viewBinders.toTypedArray().forEach {
            detachBinder(it, false)
        }
    }

    private fun getOrCreateBinder(viewModel: ViewModel): ViewBinder<*> {
        return viewBinders.firstOrNull {
            it.getViewModel() == viewModel
        } ?: attachBinder(viewModel)
    }

    private fun attachBinder(viewModel: ViewModel): ViewBinder<*> {
        val viewBinder = viewBinderFactory.getBinder(viewModel)
        setBound(viewModel, true)
        val view = viewBinder.onCreateView(container)
        view.visibility = View.VISIBLE
        viewBinder.bindView(view, viewModel)
        viewBinders.add(viewBinder)
        sortBinders()
        addViewForBinder(viewBinder)
        return viewBinder
    }

    private fun sortBinders() {
        viewBinders.sortBy {
            getItems().indexOf(it.getViewModel())
        }
    }

    private fun addViewForBinder(viewBinder: ViewBinder<*>) {
        val view = viewBinder.getView()
        val index = viewBinders.indexOf(viewBinder)
        container.addView(view, index)
    }

    private fun detachBinder(viewBinder: ViewBinder<*>, removeView: Boolean) {
        if (removeView) {
            container.removeView(viewBinder.getView())
        }
        setBound(viewBinder.getViewModel(), false)
        viewBinders.remove(viewBinder)
    }

}