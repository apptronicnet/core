package net.apptronic.core.android.view

import android.view.View
import net.apptronic.core.component.context.Context
import net.apptronic.core.view.ICoreView

internal data class RenderViewHolder(
        override val context: Context,
        override val coreView: ICoreView,
        override val frame: View,
        override val content: View
) : ViewHolder