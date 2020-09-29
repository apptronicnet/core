package net.apptronic.core.android.view.adapters

import android.content.Context
import android.view.View
import android.widget.RelativeLayout
import net.apptronic.core.android.view.IViewRenderingEngine
import net.apptronic.core.android.view.ViewHolder
import net.apptronic.core.android.view.ViewTypeAdapter
import net.apptronic.core.view.container.FrameCoreContainerView
import net.apptronic.core.view.properties.*

object FrameContainerViewAdapter : ViewTypeAdapter<FrameCoreContainerView, RelativeLayout> {

    override fun createView(context: Context, source: FrameCoreContainerView): RelativeLayout {
        return RelativeLayout(context)
    }

    override fun applyViewAttributes(engine: IViewRenderingEngine, coreView: FrameCoreContainerView, frame: View, content: RelativeLayout) {
        with(engine) {
            coreView.getChildren().forEach {
                val holder = render(it, false)
                applyChildLayoutAttributes(holder)
                content.addView(holder.frame)
            }
        }
    }

    private fun applyChildLayoutAttributes(holder: ViewHolder) {
        with(holder) {
            val layoutParams = RelativeLayout.LayoutParams(holder.frame.layoutParams)
            holder.coreView.layoutAlignmentVertical.subscribe {
                when (it) {
                    ToTop -> {
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                        layoutParams.removeRule(RelativeLayout.CENTER_VERTICAL)
                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                    }
                    ToCenter -> {
                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP)
                        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL)
                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                    }
                    ToBottom -> {
                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP)
                        layoutParams.removeRule(RelativeLayout.CENTER_VERTICAL)
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                    }
                }
                holder.frame.layoutParams = layoutParams
            }
            holder.coreView.layoutAlignmentHorizontal.subscribe {
                when (it) {
                    ToLeft -> {
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_START)
                        layoutParams.removeRule(RelativeLayout.CENTER_HORIZONTAL)
                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_END)
                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                    }
                    ToStart -> {
                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT)
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START)
                        layoutParams.removeRule(RelativeLayout.CENTER_HORIZONTAL)
                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_END)
                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                    }
                    ToCenter -> {
                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT)
                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_START)
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_END)
                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                    }
                    ToEnd -> {
                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT)
                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_START)
                        layoutParams.removeRule(RelativeLayout.CENTER_HORIZONTAL)
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END)
                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                    }
                    ToRight -> {
                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT)
                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_START)
                        layoutParams.removeRule(RelativeLayout.CENTER_HORIZONTAL)
                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_END)
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                    }
                }
                holder.frame.layoutParams = layoutParams
            }
        }
    }

}