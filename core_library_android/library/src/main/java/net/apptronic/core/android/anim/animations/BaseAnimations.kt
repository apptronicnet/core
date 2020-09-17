package net.apptronic.core.android.anim.animations

import net.apptronic.core.android.anim.transformations.*
import net.apptronic.core.android.anim.viewAnimation

val ViewAnimation_Empty = viewAnimation { }

val ViewAnimation_Fade = viewAnimation {
    alpha(0f, 1f)
}

val ViewAnimation_FromLeft = viewAnimation {
    translateXToParent(-1f, 0f)
}

val ViewAnimation_FromRight = viewAnimation {
    translateXToParent(1f, 0f)
}

val ViewAnimation_FromTop = viewAnimation {
    translateYToParent(-1f, 0f)
}

val ViewAnimation_FromBottom = viewAnimation {
    translateYToParent(1f, 0f)
}

val ViewAnimation_Scaled = viewAnimation {
    scaleX(0f, 1f)
    scaleY(0f, 1f)
}