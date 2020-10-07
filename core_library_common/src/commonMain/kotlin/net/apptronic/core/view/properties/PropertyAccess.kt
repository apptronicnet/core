package net.apptronic.core.view.properties

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.view.dimension.CoreDimension
import net.apptronic.core.view.dimension.CoreLayoutDimension

@UnderDevelopment
interface PropertyAccess {

    val FitToParent: CoreLayoutDimension
    val FitToContent: CoreLayoutDimension

    val Visible: Visibility
    val Invisible: Visibility
    val Gone: Visibility

    val Number.pixels: CoreDimension
    val Number.dimension: CoreDimension

    val ToLeft: HorizontalAlignment
    val ToStart: HorizontalAlignment
    val ToRight: HorizontalAlignment
    val ToEnd: HorizontalAlignment
    val ToTop: VerticalAlignment
    val ToBottom: VerticalAlignment
    val ToCenter: BidirectionalLayoutAlignment
    val ToCenterVertical: VerticalAlignment
    val ToCenterHorizontal: HorizontalAlignment
    val DefaultAlignment: BidirectionalLayoutAlignment

    val Thin: FontWeight
        get() = FontWeight(100)
    val Light: FontWeight
        get() = FontWeight(200)
    val Book: FontWeight
        get() = FontWeight(300)
    val Regular: FontWeight
        get() = FontWeight(400)
    val Medium: FontWeight
        get() = FontWeight(500)
    val SemiBold: FontWeight
        get() = FontWeight(600)
    val Bold: FontWeight
        get() = FontWeight(700)
    val Heavy: FontWeight
        get() = FontWeight(800)
    val UltraHeavy: FontWeight
        get() = FontWeight(900)

}