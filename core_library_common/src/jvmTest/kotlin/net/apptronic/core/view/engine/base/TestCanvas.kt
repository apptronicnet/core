package net.apptronic.core.view.engine.base

class TestCanvas(
        val canvasWidth: Int,
        val canvasHeight: Int
) {

    private val canvas: Array<Array<Char?>>

    fun clear() {
        for (array in canvas) {
            for (index in 0 until array.size) {
                array[index] = ' '
            }
        }
    }

    init {
        val arrays = mutableListOf<Array<Char?>>()
        for (x in 0 until canvasWidth) {
            arrays.add(arrayOfNulls(canvasHeight))
        }
        canvas = arrays.toTypedArray()
    }

    fun getString(y: Int): String {
        var list = mutableListOf<Char>()
        for (x in 0 until canvasWidth) {
            list.add(canvas[x][y] ?: ' ')
        }
        return String(list.toCharArray())
    }

    fun getStrings(): List<String> {
        return (0 until canvasHeight).map {
            getString(it)
        }
    }

    fun TestView.draw(x: Int, y: Int, pixel: Char) {
        if (x in 0 until this.width && y in 0 until this.height) {
            val drawX = left + x
            val drawY = top + y
            if (drawX in 0 until canvasWidth && drawY in 0 until canvasHeight) {
                canvas[drawX][drawY] = pixel
            }
        }
    }

    fun TestView.draw(x: Int, y: Int, values: String) {
        values.forEachIndexed { index, c ->
            draw(x + index, y, c)
        }
    }


}