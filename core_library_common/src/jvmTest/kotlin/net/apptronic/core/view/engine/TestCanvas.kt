package net.apptronic.core.view.engine

class TestCanvas(val width: Int, val height: Int) {

    private val canvas: Array<Array<Char?>>

    init {
        val arrays = mutableListOf<Array<Char?>>()
        for (x in 0 until width) {
            arrays.add(arrayOfNulls(height))
        }
        canvas = arrays.toTypedArray()
    }

}