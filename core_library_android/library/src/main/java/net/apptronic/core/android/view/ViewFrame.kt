package net.apptronic.core.android.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout

@SuppressLint("ViewConstructor")
internal class ViewFrame<T : View>(context: Context, val content: T) : RelativeLayout(context) {

    private var background: View? = null

    private var foreground: View? = null

    init {
        clipChildren = false
        clipToPadding = false
        content.layoutParams = LayoutParams(0, 0)
        addView(content)
    }

    fun setBackgroundView(view: View?) {
        background?.let { removeView(it) }
        this.background = view
        background?.let { addView(it, 0, LayoutParams(0, 0)) }
    }

    fun setForegroundView(view: View?) {
        foreground?.let { removeView(it) }
        this.foreground = view
        foreground?.let { addView(it, LayoutParams(0, 0)) }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        background?.layout(l, t, r, b)
        foreground?.layout(l, t, r, b)
    }

}