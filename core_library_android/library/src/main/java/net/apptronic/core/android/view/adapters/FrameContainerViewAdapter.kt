//package net.apptronic.core.android.view.adapters
//
//import android.content.Context
//import android.graphics.Color
//import android.view.View
//import android.widget.RelativeLayout
//import net.apptronic.core.android.view.IViewRenderingEngine
//import net.apptronic.core.android.view.ViewHolder
//import net.apptronic.core.android.view.ViewTypeAdapter
//import net.apptronic.core.view.container.FrameCoreContainerView
//import net.apptronic.core.view.properties.*
//
//object FrameContainerViewAdapter : ViewTypeAdapter<FrameCoreContainerView, RelativeLayout> {
//
//    override fun createView(context: Context, source: FrameCoreContainerView): RelativeLayout {
//        return RelativeLayout(context)
//    }
//
//    override fun applyViewAttributes(engine: IViewRenderingEngine, coreView: FrameCoreContainerView, frame: View, content: RelativeLayout) {
//        with(engine) {
//            coreView.getChildren().forEach {
//                val holder = render(it, false)
//                val layoutParams = RelativeLayout.LayoutParams(holder.frame.layoutParams)
//                holder.frame.layoutParams = RelativeLayout.LayoutParams(holder.frame.layoutParams)
//                applyChildLayoutAttributes(coreView, holder)
//                content.addView(holder.frame)
//            }
//        }
//    }
//
//    private fun applyChildLayoutAttributes(container: FrameCoreContainerView, holder: ViewHolder) {
//        with(holder) {
//            mergeProperties(container.contentAlignmentVertical, holder.coreView.layoutAlignmentVertical) { it1, it2 ->
//                val value = it2 ?: it1
//                val layoutParams = holder.frame.layoutParams as RelativeLayout.LayoutParams
//                when (value) {
//                    LayoutAlignment.ToTop -> {
//                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP)
//                        layoutParams.removeRule(RelativeLayout.CENTER_VERTICAL)
//                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
//                    }
//                    LayoutAlignment.ToCenter -> {
//                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP)
//                        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL)
//                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
//                    }
//                    LayoutAlignment.ToBottom -> {
//                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP)
//                        layoutParams.removeRule(RelativeLayout.CENTER_VERTICAL)
//                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
//                    }
//                }
//                holder.frame.layoutParams = layoutParams
//            }
//            mergeProperties(container.contentAlignmentHorizontal, holder.coreView.layoutAlignmentHorizontal) { it1, it2 ->
//                val value = it2 ?: it1
//                val layoutParams = holder.frame.layoutParams as RelativeLayout.LayoutParams
//                when (value) {
//                    LayoutAlignment.ToLeft -> {
//                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
//                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_START)
//                        layoutParams.removeRule(RelativeLayout.CENTER_HORIZONTAL)
//                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_END)
//                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT)
//                    }
//                    LayoutAlignment.ToStart -> {
//                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT)
//                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START)
//                        layoutParams.removeRule(RelativeLayout.CENTER_HORIZONTAL)
//                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_END)
//                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT)
//                    }
//                    LayoutAlignment.ToCenter -> {
//                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT)
//                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_START)
//                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
//                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_END)
//                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT)
//                    }
//                    LayoutAlignment.ToEnd -> {
//                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT)
//                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_START)
//                        layoutParams.removeRule(RelativeLayout.CENTER_HORIZONTAL)
//                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END)
//                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT)
//                    }
//                    LayoutAlignment.ToRight -> {
//                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT)
//                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_START)
//                        layoutParams.removeRule(RelativeLayout.CENTER_HORIZONTAL)
//                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_END)
//                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
//                    }
//                }
//                holder.frame.layoutParams = layoutParams
//            }
//        }
//    }
//
//}