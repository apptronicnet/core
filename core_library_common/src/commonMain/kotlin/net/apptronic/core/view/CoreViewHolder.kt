package net.apptronic.core.view

interface CoreViewHolder : Recyclable {

    val isRecycled: Boolean

    override fun recycle()

}