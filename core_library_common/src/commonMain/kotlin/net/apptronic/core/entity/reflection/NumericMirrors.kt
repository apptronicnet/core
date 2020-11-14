package net.apptronic.core.entity.reflection

object StringToIntMirror : Mirror<String, Int?> {

    override fun direct(value: Int?): String {
        return value?.toString() ?: ""
    }

    override fun reverse(value: String): Int? {
        return try {
            value.toInt()
        } catch (e: NumberFormatException) {
            null
        }
    }

}

val IntToStringMirror = StringToIntMirror.inverted

object StringToLongMirror : Mirror<String, Long?> {

    override fun direct(value: Long?): String {
        return value?.toString() ?: ""
    }

    override fun reverse(value: String): Long? {
        return try {
            value.toLong()
        } catch (e: NumberFormatException) {
            null
        }
    }

}

val LongToStringMirror = StringToLongMirror.inverted

object StringToFloatMirror : Mirror<String, Float?> {

    override fun direct(value: Float?): String {
        return value?.toString() ?: ""
    }

    override fun reverse(value: String): Float? {
        return try {
            value.toFloat()
        } catch (e: NumberFormatException) {
            null
        }
    }

}

val FloatToStringMirror = StringToFloatMirror.inverted

object StringToDoubleMirror : Mirror<String, Double?> {

    override fun direct(value: Double?): String {
        return value?.toString() ?: ""
    }

    override fun reverse(value: String): Double? {
        return try {
            value.toDouble()
        } catch (e: NumberFormatException) {
            null
        }
    }

}

val DoubleToStringMirror = StringToDoubleMirror.inverted