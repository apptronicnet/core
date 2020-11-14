package net.apptronic.core.entity.reflection

object StringToIntMirror : Mirror<String, Int?> {

    override fun direct(value: String): Int? {
        return try {
            value.toInt()
        } catch (e: NumberFormatException) {
            null
        }
    }

    override fun reverse(value: Int?): String {
        return value?.toString() ?: ""
    }

}

val IntToStringMirror = StringToIntMirror.inverted

object StringToLongMirror : Mirror<String, Long?> {

    override fun direct(value: String): Long? {
        return try {
            value.toLong()
        } catch (e: NumberFormatException) {
            null
        }
    }

    override fun reverse(value: Long?): String {
        return value?.toString() ?: ""
    }

}

val LongToStringMirror = StringToLongMirror.inverted

object StringToFloatMirror : Mirror<String, Float?> {

    override fun direct(value: String): Float? {
        return try {
            value.toFloat()
        } catch (e: NumberFormatException) {
            null
        }
    }

    override fun reverse(value: Float?): String {
        return value?.toString() ?: ""
    }

}

val FloatToStringMirror = StringToFloatMirror.inverted

object StringToDoubleMirror : Mirror<String, Double?> {

    override fun direct(value: String): Double? {
        return try {
            value.toDouble()
        } catch (e: NumberFormatException) {
            null
        }
    }

    override fun reverse(value: Double?): String {
        return value?.toString() ?: ""
    }

}

val DoubleToStringMirror = StringToDoubleMirror.inverted