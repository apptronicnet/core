package net.apptronic.core.android.view

import android.view.View
import net.apptronic.core.component.context.Context
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.ViewPropertyConsumer

interface ViewHolder : ViewPropertyConsumer {
    override val context: Context
    val coreView: ICoreView
    val frame: View
    val content: View
}