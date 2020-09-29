package net.apptronic.core.view.properties

import net.apptronic.core.view.dimension.CoreDimension
import net.apptronic.core.view.dimension.CoreLayoutSpec

interface PropertyAccess {

    val FitToParent: CoreLayoutSpec
    val FitToContent: CoreLayoutSpec

    val Visible: Visibility
    val Invisible: Visibility
    val Gone: Visibility

    val Number.pixels: CoreDimension
    val Number.dimension: CoreDimension
    val CoreDimensionZero: CoreDimension

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

}