package net.apptronic.core.android.viewmodel.bindings

import android.view.View
import net.apptronic.core.android.anim.AnimationPlayer
import net.apptronic.core.android.anim.ViewAnimationDefinition
import net.apptronic.core.android.anim.transition.ViewTransitionDefinition
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.base.observable.distinctUntilChanged
import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.mvvm.viewmodel.ViewModel

fun BindingContainer.bindVisibleInvisible(
    view: View, target: Entity<Boolean>, containerView: View? = null,
    animationBuilder: VisibilityAnimator.() -> Unit = {}
) {
    +VisibilityBinding(view, target, containerView, View.INVISIBLE, animationBuilder)
}

fun BindingContainer.bindVisibleGone(
    view: View, target: Entity<Boolean>, containerView: View? = null,
    animationBuilder: VisibilityAnimator.() -> Unit = {}
) {
    +VisibilityBinding(view, target, containerView, View.GONE, animationBuilder)
}

private class VisibilityBinding(
    private val view: View,
    private val target: Entity<Boolean>,
    private val containerView: View?,
    private val invisibleState: Int,
    private val animationBuilder: VisibilityAnimator.() -> Unit = {}
) : Binding() {

    override fun onBind(viewModel: ViewModel, viewBinder: ViewBinder<*>) {
        val container = containerView ?: view.parent as? View ?: view
        val player = AnimationPlayer(container)
        val animator = VisibilityAnimator(view, container)
        animator.animationBuilder()
        target.distinctUntilChanged().subscribe { isVisible ->
            if (viewModel.isVisible()) {
                if (isVisible) {
                    animator.show(player)
                } else {
                    animator.hide(player, invisibleState)
                }
            } else {
                player.cancelAnimations(view)
                view.visibility = if (isVisible) View.VISIBLE else invisibleState
            }
        }
        onUnbind {
            player.recycle()
        }
    }

}

class VisibilityAnimator internal constructor(
    private val target: View,
    private val container: View
) {

    fun intercept(entity: Entity<Boolean>) {
        entity.subscribe {
            intercept = it
        }
    }

    var intercept = true

    var animation: ViewAnimationDefinition? = null
        set(value) {
            field = value
            enter = value
            exit = value?.reversed()
        }

    var viewTransition: ViewTransitionDefinition? = null
        set(value) {
            field = value
            enter = value?.enter
            exit = value?.exit
        }

    var enter: ViewAnimationDefinition? = null

    var exit: ViewAnimationDefinition? = null

    var duration: Long =
        target.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
        set(value) {
            field = value
            enterDuration = value
            exitDuration = value
        }

    var enterDuration: Long = duration

    var exitDuration: Long = duration

    internal fun show(player: AnimationPlayer) {
        val enter = this.enter
        if (enter == null) {
            player.cancelAnimations(target)
            target.visibility = View.VISIBLE
        } else {
            val animation = enter.createAnimation(target, container, enterDuration)
            animation.doOnStart {
                target.visibility = View.VISIBLE
            }.playOn(player, intercept)
        }
    }

    internal fun hide(player: AnimationPlayer, targetVisibility: Int) {
        val exit = this.exit
        if (exit == null) {
            player.cancelAnimations(target)
            target.visibility = targetVisibility
        } else {
            val animation = exit.createAnimation(target, container, exitDuration)
            animation.doOnComplete {
                target.visibility = targetVisibility
            }.playOn(player, intercept)
        }
    }
}