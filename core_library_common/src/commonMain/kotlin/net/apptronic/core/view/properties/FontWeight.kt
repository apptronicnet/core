package net.apptronic.core.view.properties

data class FontWeight(val weight: Int) {
    val Thin = FontWeight(100)
    val Light = FontWeight(200)
    val Book = FontWeight(300)
    val Regular = FontWeight(400)
    val Medium = FontWeight(500)
    val SemiBold = FontWeight(600)
    val Bold = FontWeight(700)
    val Black = FontWeight(800)
    val UltraBlack = FontWeight(900)
}