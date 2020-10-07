package net.apptronic.core.view.properties

import net.apptronic.core.UnderDevelopment

@UnderDevelopment
interface ILayoutAlignment

@UnderDevelopment
interface HorizontalAlignment : ILayoutAlignment

@UnderDevelopment
interface VerticalAlignment : ILayoutAlignment

@UnderDevelopment
interface BidirectionalLayoutAlignment : HorizontalAlignment, VerticalAlignment

@UnderDevelopment
object LayoutAlignment {
    object ToLeft : HorizontalAlignment
    object ToStart : HorizontalAlignment
    object ToRight : HorizontalAlignment
    object ToEnd : HorizontalAlignment
    object ToTop : VerticalAlignment
    object ToBottom : VerticalAlignment
    object ToCenter : BidirectionalLayoutAlignment

    val ToCenterVertical: VerticalAlignment = ToCenter
    val ToCenterHorizontal: HorizontalAlignment = ToCenter
    val DefaultAlignment = ToCenter
}

