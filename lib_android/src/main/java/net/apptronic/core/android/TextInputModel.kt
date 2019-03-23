//package net.apptronic.core.android
//
//import net.apptronic.core.component.Component
//
//class TextInputModel(parent: Component) : Component.SubModel(parent) {
//
//    val inputText = value<String>("")
//    val textColor = value<Color>()
//    val hint = value<Text>()
//
//    fun getText(): String {
//        return inputText.getOrNull() ?: ""
//    }
//
//}