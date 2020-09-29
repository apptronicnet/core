package net.apptronic.core.android.view

import android.view.View
import android.view.ViewGroup
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.dimension.CoreDimension
import net.apptronic.core.view.dimension.CoreLayoutDimension
import net.apptronic.core.view.dimension.FitToContentDimension
import net.apptronic.core.view.dimension.FitToParentDimension
import net.apptronic.core.view.properties.Visibility

internal class ViewFrameFactory {

    private fun IViewRenderingEngine.toLayoutParam(dimension: CoreLayoutDimension): Int {
        return when (dimension) {
            is CoreDimension -> dimensionEngine.getDimensionPixelSizeInt(dimension)
            is FitToParentDimension -> android.view.ViewGroup.LayoutParams.MATCH_PARENT
            is FitToContentDimension -> android.view.ViewGroup.LayoutParams.WRAP_CONTENT
            else -> 0
        }
    }

    private fun IViewRenderingEngine.setPaddings(coreView: ICoreView, content: View) {
        val top = dimensionEngine.getDimensionPixelSizeInt(coreView.paddingTop.getValue())
        val bottom = dimensionEngine.getDimensionPixelSizeInt(coreView.paddingBottom.getValue())
        val start = dimensionEngine.getDimensionPixelSizeInt(coreView.paddingStart.getValue())
        val end = dimensionEngine.getDimensionPixelSizeInt(coreView.paddingEnd.getValue())
        content.setPaddingRelative(start, top, end, bottom)
    }

    private fun IViewRenderingEngine.setIndents(coreView: ICoreView, viewFrame: ViewFrame<*>) {
        val top = dimensionEngine.getDimensionPixelSizeInt(coreView.indentTop.getValue())
        val bottom = dimensionEngine.getDimensionPixelSizeInt(coreView.indentBottom.getValue())
        val start = dimensionEngine.getDimensionPixelSizeInt(coreView.indentStart.getValue())
        val end = dimensionEngine.getDimensionPixelSizeInt(coreView.indentEnd.getValue())
        viewFrame.setPaddingRelative(start, top, end, bottom)
    }

    fun <T : View> createViewFrame(engine: IViewRenderingEngine, coreView: ICoreView, content: T): ViewFrame<T> {
        val viewFrame = ViewFrame(engine.androidContext, content)
        viewFrame.layoutParams = ViewGroup.LayoutParams(0, 0)
        with(engine) {
            coreView.width.subscribe {
                viewFrame.layoutParams.width = toLayoutParam(it)
                content.layoutParams.width = toLayoutParam(it)
                content.requestLayout()
            }
            coreView.height.subscribe {
                viewFrame.layoutParams.height = toLayoutParam(it)
                content.layoutParams.height = toLayoutParam(it)
                content.requestLayout()
            }
            coreView.indentTop.subscribe { setIndents(coreView, viewFrame) }
            coreView.indentBottom.subscribe { setIndents(coreView, viewFrame) }
            coreView.indentStart.subscribe { setIndents(coreView, viewFrame) }
            coreView.indentEnd.subscribe { setIndents(coreView, viewFrame) }

            coreView.paddingStart.subscribe { setPaddings(coreView, content) }
            coreView.paddingEnd.subscribe { setPaddings(coreView, content) }
            coreView.paddingTop.subscribe { setPaddings(coreView, content) }
            coreView.paddingBottom.subscribe { setPaddings(coreView, content) }

            coreView.shadow.subscribe { content.elevation = dimensionEngine.getDimensionPixelSizeFloat(it) }
            coreView.visibility.subscribe {
                content.visibility = when (it) {
                    Visibility.Visible -> View.VISIBLE
                    Visibility.Invisible -> View.INVISIBLE
                    Visibility.Gone -> View.GONE
                }
            }
            coreView.background.subscribe {
                if (it != null) {
                    val holder = render(it, false)
                    viewFrame.setBackgroundView(holder.frame)
                } else {
                    viewFrame.setBackgroundView(null)
                }
            }
            coreView.foreground.subscribe {
                if (it != null) {
                    val holder = render(it, false)
                    viewFrame.setForegroundView(holder.frame)
                } else {
                    viewFrame.setForegroundView(null)
                }
            }
        }
        return viewFrame
    }

}