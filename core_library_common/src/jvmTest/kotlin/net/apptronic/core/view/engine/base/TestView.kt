package net.apptronic.core.view.engine.base

abstract class TestView {

    var paddingLeft: Int = 0
    var paddingRight: Int = 0
    var paddingTop: Int = 0
    var paddingBottom: Int = 0

    var left: Int = 0
    var top: Int = 0
    open val width: Int = 0
    open val height: Int = 0

    abstract fun draw(canvas: TestCanvas)

    abstract fun refreshLayout()

}