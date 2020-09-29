package net.apptronic.core.view.properties

interface ILayoutAlignment

interface HorizontalAlignment : ILayoutAlignment
interface VerticalAlignment : ILayoutAlignment
interface BidirectionalLayoutAlignment : HorizontalAlignment, VerticalAlignment

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

