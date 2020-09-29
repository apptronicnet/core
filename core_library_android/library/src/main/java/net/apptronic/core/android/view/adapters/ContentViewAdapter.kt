package net.apptronic.core.android.view.adapters

import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import net.apptronic.core.android.view.IViewRenderingEngine
import net.apptronic.core.android.view.ViewTypeAdapter
import net.apptronic.core.android.view.platform.getGravityInt
import net.apptronic.core.view.ICoreContentView

object ContentViewAdapter : ViewTypeAdapter<ICoreContentView, View> {

    private fun IViewRenderingEngine.withGravity(coreView: ICoreContentView, action: (Int) -> Unit) {
        coreView.contentAlignmentHorizontal.subscribe {
            val gravity = coreView.contentAlignmentVertical.getValue().getGravityInt() or it.getGravityInt()
            action(gravity)
        }
        coreView.contentAlignmentVertical.subscribe {
            val gravity = coreView.contentAlignmentHorizontal.getValue().getGravityInt() or it.getGravityInt()
            action(gravity)
        }
    }

    override fun applyViewAttributes(engine: IViewRenderingEngine, coreView: ICoreContentView, frame: View, content: View) {
        with(engine) {
            if (content is TextView) {
                withGravity(coreView) {
                    content.gravity = it
                }
            }
            if (content is LinearLayout) {
                withGravity(coreView) {
                    content.gravity = it
                }
            }
        }
    }

}