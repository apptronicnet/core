//package net.apptronic.core.android
//
//import android.content.Context
//import androidx.annotation.ColorRes
//import androidx.core.content.ContextCompat
//
//abstract class Color {
//
//    companion object {
//        fun ofValue(color: Int): Color {
//            return ValueColor(color)
//        }
//
//        fun ofResource(@ColorRes colorRes: Int): Color {
//            return ResourceColor(colorRes)
//        }
//    }
//
//    abstract fun getValue(context: Context): Int
//
//    private class ValueColor(private val value: Int) : Color() {
//        override fun getValue(context: Context): Int {
//            return value
//        }
//    }
//
//    private class ResourceColor(@ColorRes private val colorRes: Int) : Color() {
//        override fun getValue(context: Context): Int {
//            return ContextCompat.getColor(context, colorRes)
//        }
//    }
//
//}