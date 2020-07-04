package net.apptronic.core.android.viewmodel.transitions

import android.view.View
import net.apptronic.core.base.android.R

abstract class ViewTransition : Transition<View>() {

    override fun getRunningTransition(target: View): Transition<View>? {
        return target.getTag(R.id.TransitionAnimation) as? Transition<View>
    }

    override fun setRunningTransition(target: View) {
        target.setTag(R.id.TransitionAnimation, this)
    }

    override fun clearRunningTransition(target: View) {
        target.setTag(R.id.TransitionAnimation, null)
    }

    override fun isAllowsTransition(target: View): Boolean {
        return target.handler != null
    }

}