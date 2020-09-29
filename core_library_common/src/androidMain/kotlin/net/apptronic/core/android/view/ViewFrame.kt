package net.apptronic.core.android.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.FrameLayout

@SuppressLint("ViewConstructor")
internal class ViewFrame<T : View>(context: Context, val content: T) : FrameLayout(context) {

    private var background: View? = null

    private var foreground: View? = null

    init {
        clipChildren = false
        clipToPadding = false
        content.layoutParams = FrameLayout.LayoutParams(0, 0)
        addView(content)
    }

    fun setBackgroundView(view: View?) {
        background?.let { removeView(it) }
        this.background = view
        background?.let { addView(it, 0) }
    }

    fun setForegroundView(view: View?) {
        foreground?.let { removeView(it) }
        this.foreground = view
        background?.let { addView(it) }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        content.layout(left, top, right, bottom)
        setMeasuredDimension(content.measuredWidth, content.measuredHeight)
        background?.setMeasuredDimension(content.measuredWidth, content.measuredHeight)
        foreground?.setMeasuredDimension(content.measuredWidth, content.measuredHeight)
    }

}