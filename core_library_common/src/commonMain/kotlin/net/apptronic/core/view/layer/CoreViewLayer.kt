package net.apptronic.core.view.layer

import net.apptronic.core.view.CoreViewHolder
import net.apptronic.core.view.CoreViewSet
import net.apptronic.core.view.properties.CoreColor
import net.apptronic.core.view.shape.CoreDrawable

sealed class CoreViewLayer : CoreViewHolder {
    class Color(val color: CoreColor) : CoreViewLayer() {
        private var isRecycledState = false

        override val isRecycled: Boolean
            get() = isRecycledState

        override fun recycle() {
            isRecycledState = true
        }
    }

    class Drawable(val drawable: CoreDrawable) : CoreViewLayer() {
        private var isRecycledState = false

        override val isRecycled: Boolean
            get() = isRecycledState

        override fun recycle() {
            isRecycledState = true
        }
    }

    class ViewSet(val set: CoreViewSet) : CoreViewLayer() {
        private var isRecycledState = false

        override val isRecycled: Boolean
            get() = set.isRecycled

        override fun recycle() {
            set.recycle()
        }
    }
}

