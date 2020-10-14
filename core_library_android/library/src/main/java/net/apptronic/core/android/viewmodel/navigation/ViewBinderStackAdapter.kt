package net.apptronic.core.android.viewmodel.navigation

import android.view.View
import android.view.ViewGroup
import net.apptronic.core.android.anim.AnimationPlayer
import net.apptronic.core.android.anim.factory.*
import net.apptronic.core.android.anim.transition.ViewTransitionDirection
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.android.viewmodel.view.ViewContainerDelegate
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.SingleViewModelAdapter
import net.apptronic.core.mvvm.viewmodel.navigation.StackNavigator
import net.apptronic.core.mvvm.viewmodel.navigation.TransitionInfo
import net.apptronic.core.mvvm.viewmodel.navigation.ViewModelItem

/**
 * Adapter for [StackNavigator]
 *
 * @param container in which [View] should be added
 * @param viewBinderFactory to create [ViewBinder] for [ViewModel]
 * @param viewTransitionFactory for creating animations
 */
open class ViewBinderStackAdapter(
    private val container: ViewGroup,
    private val viewBinderFactory: ViewBinderFactory = ViewBinderFactory(),
    private val viewTransitionFactory: ViewTransitionFactory = BasicViewTransitionFactory,
    private val defaultAnimationTime: Long =
        container.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong(),
) : SingleViewModelAdapter {

    private val player = AnimationPlayer(container)

    fun bindings(setup: ViewBinderFactory.() -> Unit) {
        setup.invoke(viewBinderFactory)
    }

    private var currentBinder: ViewBinder<*>? = null

    override fun onInvalidate(item: ViewModelItem?, transitionInfo: TransitionInfo) {
        val newModel = item?.viewModel
        val newBinder = if (newModel != null) viewBinderFactory.getBinder(newModel) else null
        if (newBinder != null && newModel != null) {
            val delegate = newBinder.getViewDelegate<ViewContainerDelegate<*>>()
            val view =
                delegate.performCreateView(newModel, newBinder, container.context, null, container)
            newBinder.performViewBinding(newModel, view)
        }
        setView(newBinder, transitionInfo.isNewOnFront, transitionInfo.spec)
    }

    private fun setView(newBinder: ViewBinder<*>?, isNewOnFront: Boolean, transitionSpec: Any?) {
        val oldBinder = currentBinder
        currentBinder = newBinder
        if (oldBinder != null && newBinder != null) {
            onReplace(
                newBinder.getViewModel(),
                newBinder,
                container,
                oldBinder.getView(),
                newBinder.getView(),
                isNewOnFront,
                transitionSpec
            )
        } else if (newBinder != null) {
            onAdd(
                newBinder.getViewModel(),
                newBinder, container, newBinder.getView(), transitionSpec
            )
        } else if (oldBinder != null) {
            onRemove(
                oldBinder.getViewModel(),
                oldBinder, container, oldBinder.getView(), transitionSpec
            )
        }
    }

    open fun onAdd(
        viewModel: IViewModel,
        viewBinder: ViewBinder<*>,
        container: ViewGroup,
        newView: View,
        transitionSpec: Any?
    ) {
        val spec = SingleTransitionSpec(
            target = newView,
            container = container,
            duration = defaultAnimationTime,
            transitionSpec = transitionSpec
        )
        val animation = viewTransitionFactory.buildSingleEnterOrEmpty(spec)
        animation.doOnStart {
            addViewToContainer(viewModel, viewBinder, container, newView, true)
            newView.visibility = View.VISIBLE
        }.playOn(player, true)
    }

    open fun onReplace(
        viewModel: IViewModel,
        viewBinder: ViewBinder<*>,
        container: ViewGroup,
        oldView: View,
        newView: View,
        isNewOnFront: Boolean,
        transitionSpec: Any?
    ) {
        val spec = ViewTransitionSpec(
            enter = newView,
            exit = oldView,
            container = container,
            duration = defaultAnimationTime,
            transitionSpec = transitionSpec,
            direction = if (isNewOnFront)
                ViewTransitionDirection.EnteringOnFront
            else
                ViewTransitionDirection.ExitingOnFront
        )
        val transition = viewTransitionFactory.buildViewTransitionOrEmpty(spec)
        transition.viewAnimationSet.getAnimation(newView)?.doOnStart {
            val addNewOnFront = transition.direction == ViewTransitionDirection.EnteringOnFront
            addViewToContainer(viewModel, viewBinder, container, newView, addNewOnFront)
            newView.visibility = View.VISIBLE
        }
        transition.viewAnimationSet.getAnimation(oldView)?.doOnCompleteOrCancel {
            val delegate = viewBinder.getViewDelegate<ViewContainerDelegate<*>>()
            delegate.performDetachView(viewModel, viewBinder, oldView, container)
        }
        transition.viewAnimationSet.playOn(player, true)
    }

    open fun onRemove(
        viewModel: IViewModel,
        viewBinder: ViewBinder<*>,
        container: ViewGroup,
        oldView: View,
        transitionSpec: Any?
    ) {
        val spec = SingleTransitionSpec(
            target = oldView,
            container = container,
            duration = defaultAnimationTime,
            transitionSpec = transitionSpec
        )
        val animation = viewTransitionFactory.buildSingleExitOrEmpty(spec)
        animation.doOnCompleteOrCancel {
            container.removeView(oldView)
        }
        animation.playOn(player, true)
    }

    private fun addViewToContainer(
        viewModel: IViewModel,
        viewBinder: ViewBinder<*>,
        container: ViewGroup,
        child: View,
        toFront: Boolean
    ) {
        val delegate = viewBinder.getViewDelegate<ViewContainerDelegate<*>>()
        val position = if (toFront) {
            container.childCount
        } else {
            0
        }
        delegate.performAttachView(viewModel, child, container, position)
    }

}