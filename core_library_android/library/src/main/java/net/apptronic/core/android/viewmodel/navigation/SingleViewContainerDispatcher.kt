package net.apptronic.core.android.viewmodel.navigation

import android.view.View
import android.view.ViewGroup
import net.apptronic.core.android.anim.AnimationPlayer
import net.apptronic.core.android.anim.factory.*
import net.apptronic.core.android.anim.transition.ViewTransitionDirection
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.view.DefaultViewContainerViewAdapter
import net.apptronic.core.android.viewmodel.view.ViewContainerViewAdapter
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.TransitionInfo

internal class SingleViewContainerDispatcher(
    private val container: ViewGroup,
    private val viewTransitionFactory: ViewTransitionFactory,
    private val defaultAnimationTime: Long,
) {

    private val player = AnimationPlayer(container)

    fun setView(
        oldBinder: ViewBinder<*>?,
        newBinder: ViewBinder<*>?,
        transitionInfo: TransitionInfo
    ) {
        if (oldBinder != null && newBinder != null) {
            replace(
                oldBinder,
                newBinder,
                transitionInfo.isNewOnFront,
                transitionInfo.spec
            )
        } else if (newBinder != null) {
            add(newBinder, transitionInfo.spec)
        } else if (oldBinder != null) {
            remove(oldBinder, transitionInfo.spec)
        }
    }

    private fun add(
        viewBinder: ViewBinder<*>,
        transitionSpec: Any?
    ) {
        val spec = SingleTransitionSpec(
            target = viewBinder.getView(),
            container = container,
            duration = defaultAnimationTime,
            transitionSpec = transitionSpec
        )
        val animation = viewTransitionFactory.buildSingleEnterOrEmpty(spec)
        animation.doOnStart {
            addViewToContainer(viewBinder.getViewModel(), viewBinder, viewBinder.getView(), true)
            viewBinder.getView().visibility = View.VISIBLE
        }.playOn(player, true)
    }

    private fun replace(
        oldBinder: ViewBinder<*>,
        newBinder: ViewBinder<*>,
        isNewOnFront: Boolean,
        transitionSpec: Any?
    ) {
        val spec = ViewTransitionSpec(
            enter = newBinder.getView(),
            exit = oldBinder.getView(),
            container = container,
            duration = defaultAnimationTime,
            transitionSpec = transitionSpec,
            direction = if (isNewOnFront)
                ViewTransitionDirection.EnteringOnFront
            else
                ViewTransitionDirection.ExitingOnFront
        )
        val transition = viewTransitionFactory.buildViewTransitionOrEmpty(spec)
        transition.viewAnimationSet.getAnimation(newBinder.getView())?.doOnStart {
            val addNewOnFront = transition.direction == ViewTransitionDirection.EnteringOnFront
            addViewToContainer(
                newBinder.getViewModel(),
                newBinder,
                newBinder.getView(),
                addNewOnFront
            )
            newBinder.getView().visibility = View.VISIBLE
        }
        transition.viewAnimationSet.getAnimation(oldBinder.getView())?.doOnCompleteOrCancel {
            val viewAdapter =
                oldBinder as? ViewContainerViewAdapter ?: DefaultViewContainerViewAdapter
            viewAdapter.onDetachView(
                oldBinder.getViewModel(),
                oldBinder,
                oldBinder.getView(),
                container
            )
        }
        transition.viewAnimationSet.playOn(player, true)
    }

    private fun remove(
        viewBinder: ViewBinder<*>,
        transitionSpec: Any?
    ) {
        val spec = SingleTransitionSpec(
            target = viewBinder.getView(),
            container = container,
            duration = defaultAnimationTime,
            transitionSpec = transitionSpec
        )
        val animation = viewTransitionFactory.buildSingleExitOrEmpty(spec)
        animation.doOnCompleteOrCancel {
            val viewAdapter =
                viewBinder as? ViewContainerViewAdapter ?: DefaultViewContainerViewAdapter
            viewAdapter.onDetachView(
                viewBinder.getViewModel(),
                viewBinder,
                viewBinder.getView(),
                container
            )
        }
        animation.playOn(player, true)
    }

    private fun addViewToContainer(
        viewModel: IViewModel,
        viewBinder: ViewBinder<*>,
        child: View,
        toFront: Boolean
    ) {
        val viewAdapter = viewBinder as? ViewContainerViewAdapter ?: DefaultViewContainerViewAdapter
        if (child.parent != null) {
            container.removeView(child)
        }
        val position = if (toFront) {
            container.childCount
        } else {
            0
        }
        viewAdapter.onAttachView(viewModel, child, container, position)
    }

}