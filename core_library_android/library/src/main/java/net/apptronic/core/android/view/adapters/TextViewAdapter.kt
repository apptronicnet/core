package net.apptronic.core.android.view.adapters

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import net.apptronic.core.android.view.IViewRenderingEngine
import net.apptronic.core.android.view.ViewTypeAdapter
import net.apptronic.core.android.view.platform.getAndroidColor
import net.apptronic.core.view.widgets.CoreTextView

object TextViewAdapter : ViewTypeAdapter<CoreTextView, TextView> {

    override fun createView(context: Context, source: CoreTextView): TextView {
        return AppCompatTextView(context)
    }

    override fun applyViewAttributes(engine: IViewRenderingEngine, coreView: CoreTextView, frame: View, content: TextView) {
        with(engine) {
            coreView.text.subscribe {
                content.text = it
            }
            coreView.textSize.subscribe {
                content.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimensionEngine.getDimensionPixelSizeFloat(it))
            }
            coreView.textColor.subscribe {
                content.setTextColor(it.getAndroidColor())
            }
        }
    }

}