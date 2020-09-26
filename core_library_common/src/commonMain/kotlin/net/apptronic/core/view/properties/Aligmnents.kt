package net.apptronic.core.view.properties

interface LayoutAlignment

interface HorizontalAlignment : LayoutAlignment
interface VerticalAlignment : LayoutAlignment

object ToLeft : HorizontalAlignment
object ToStart : HorizontalAlignment
object ToRight : HorizontalAlignment
object ToEnd : HorizontalAlignment
object ToTop : VerticalAlignment
object ToBottom : VerticalAlignment
object ToCenter : HorizontalAlignment, VerticalAlignment

val ToCenterVertical: HorizontalAlignment = ToCenter
val ToCenterHorizontal: VerticalAlignment = ToCenter

val DefaultAlignment = ToCenter