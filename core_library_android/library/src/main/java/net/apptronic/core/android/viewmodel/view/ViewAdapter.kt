package net.apptronic.core.android.viewmodel.view

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import net.apptronic.core.android.viewmodel.ViewBinder

interface ViewAdapter {

    fun onCreateView(
        viewBinder: ViewBinder<*>, context: Context, inflater: LayoutInflater, container: ViewGroup?
    ): View {
        val layoutResId = viewBinder.layoutResId
        return if (layoutResId != null) {
            inflater.inflate(layoutResId, container, false)
        } else {
            createFallbackView(context, this::class.simpleName ?: "ViewBinder")
        }
    }

    fun createFallbackView(context: Context, fallbackText: String): View {
        return TextView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
            setTextColor(Color.BLACK)
            setBackgroundColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            text = fallbackText
        }
    }
}