package net.apptronic.core.entity.reflection

import net.apptronic.core.entity.base.MutableValue
import net.apptronic.core.entity.base.Value

fun Value<String>.reflectAsInt(): Value<Int?> = reflect(StringToIntMirror)

fun MutableValue<String>.reflectAsIntMutable(): MutableValue<Int?> = reflectMutable(StringToIntMirror)

fun Value<String>.reflectAsLong(): Value<Long?> = reflect(StringToLongMirror)

fun MutableValue<String>.reflectAsLongMutable(): MutableValue<Long?> = reflectMutable(StringToLongMirror)

fun Value<String>.reflectAsFloat(): Value<Float?> = reflect(StringToFloatMirror)

fun MutableValue<String>.reflectAsFloatMutable(): MutableValue<Float?> = reflectMutable(StringToFloatMirror)

fun Value<String>.reflectAsDouble(): Value<Double?> = reflect(StringToDoubleMirror)

fun MutableValue<String>.reflectAsDoubleMutable(): MutableValue<Double?> = reflectMutable(StringToDoubleMirror)

object StringToIntMirror : Mirror<String, Int?> {

    override fun direct(value: String): Int? {
        return value.toIntOrNull()
    }

    override fun reverse(value: Int?): String {
        return value?.toString() ?: ""
    }

}

val IntToStringMirror = StringToIntMirror.inverted

object StringToLongMirror : Mirror<String, Long?> {

    override fun direct(value: String): Long? {
        return value.toLongOrNull()
    }

    override fun reverse(value: Long?): String {
        return value?.toString() ?: ""
    }

}

val LongToStringMirror = StringToLongMirror.inverted

object StringToFloatMirror : Mirror<String, Float?> {

    override fun direct(value: String): Float? {
        return value.toFloatOrNull()
    }

    override fun reverse(value: Float?): String {
        return value?.toString() ?: ""
    }

}

val FloatToStringMirror = StringToFloatMirror.inverted

object StringToDoubleMirror : Mirror<String, Double?> {

    override fun direct(value: String): Double? {
        return value.toDoubleOrNull()
    }

    override fun reverse(value: Double?): String {
        return value?.toString() ?: ""
    }

}

val DoubleToStringMirror = StringToDoubleMirror.inverted