package net.apptronic.core.view.properties

import net.apptronic.core.view.dimension.FitToContentDimension
import net.apptronic.core.view.dimension.FitToParentDimension

interface PropertyAccess {

    val FitToParent: FitToParentDimension
        get() = FitToParentDimension()

    val FitToContent: FitToContentDimension
        get() = FitToContentDimension()

    val Visible: Visibility
        get() = Visibility.Visible

    val Invisible: Visibility
        get() = Visibility.Invisible

    val Gone: Visibility
        get() = Visibility.Gone


}