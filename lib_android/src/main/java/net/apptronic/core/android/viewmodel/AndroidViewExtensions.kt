package net.apptronic.core.android.viewmodel

import android.view.View
import android.view.ViewGroup
import android.widget.*

fun AndroidView<*>.view(id: Int): View = findView(id)

fun AndroidView<*>.textView(id: Int): TextView = findView(id)

fun AndroidView<*>.editText(id: Int): TextView = findView(id)

fun AndroidView<*>.imageView(id: Int): ImageView = findView(id)

fun AndroidView<*>.viewGroup(id: Int): ViewGroup = findView(id)

fun AndroidView<*>.frameLayout(id: Int): FrameLayout = findView(id)

fun AndroidView<*>.linearLayout(id: Int): LinearLayout = findView(id)

fun AndroidView<*>.tableLayout(id: Int): TableLayout = findView(id)

fun AndroidView<*>.relativeLayout(id: Int): RelativeLayout = findView(id)

