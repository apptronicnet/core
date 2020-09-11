package net.apptronic.core.android.viewmodel.anim.animations

import net.apptronic.core.android.viewmodel.anim.transformations.*
import net.apptronic.core.android.viewmodel.anim.viewAnimation

val Animation_Empty = viewAnimation { }

val Animation_Fade = viewAnimation {
    alpha(0f, 1f)
}

val Animation_FromLeft = viewAnimation {
    translateXToParent(-1f, 0f)
}

val Animation_FromRight = viewAnimation {
    translateXToParent(1f, 0f)
}

val Animation_FromTop = viewAnimation {
    translateYToParent(-1f, 0f)
}

val Animation_FromBottom = viewAnimation {
    translateYToParent(1f, 0f)
}

val Animation_Scaled = viewAnimation {
    scaleX(0f, 1f)
    scaleY(0f, 1f)
}