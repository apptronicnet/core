package net.apptronic.core.entity.association

import net.apptronic.core.entity.base.MutableValue
import net.apptronic.core.entity.base.Value

fun Value<String>.associateAsInt(): Value<Int?> = associate(StringToIntAssociation)

fun MutableValue<String>.associateAsIntMutable(): MutableValue<Int?> = associateMutable(StringToIntAssociation)

fun Value<String>.associateAsLong(): Value<Long?> = associate(StringToLongAssociation)

fun MutableValue<String>.associateAsLongMutable(): MutableValue<Long?> = associateMutable(StringToLongAssociation)

fun Value<String>.associateAsFloat(): Value<Float?> = associate(StringToFloatAssociation)

fun MutableValue<String>.associateAsFloatMutable(): MutableValue<Float?> = associateMutable(StringToFloatAssociation)

fun Value<String>.associateAsDouble(): Value<Double?> = associate(StringToDoubleAssociation)

fun MutableValue<String>.associateAsDoubleMutable(): MutableValue<Double?> = associateMutable(StringToDoubleAssociation)

object StringToIntAssociation : Association<String, Int?> {

    override fun direct(value: String): Int? {
        return value.toIntOrNull()
    }

    override fun reverse(value: Int?): String {
        return value?.toString() ?: ""
    }

}

val IntToStringAssociation = StringToIntAssociation.inverted

object StringToLongAssociation : Association<String, Long?> {

    override fun direct(value: String): Long? {
        return value.toLongOrNull()
    }

    override fun reverse(value: Long?): String {
        return value?.toString() ?: ""
    }

}

val LongToStringAssociation = StringToLongAssociation.inverted

object StringToFloatAssociation : Association<String, Float?> {

    override fun direct(value: String): Float? {
        return value.toFloatOrNull()
    }

    override fun reverse(value: Float?): String {
        return value?.toString() ?: ""
    }

}

val FloatToStringAssociation = StringToFloatAssociation.inverted

object StringToDoubleAssociation : Association<String, Double?> {

    override fun direct(value: String): Double? {
        return value.toDoubleOrNull()
    }

    override fun reverse(value: Double?): String {
        return value?.toString() ?: ""
    }

}

val DoubleToStringAssociation = StringToDoubleAssociation.inverted