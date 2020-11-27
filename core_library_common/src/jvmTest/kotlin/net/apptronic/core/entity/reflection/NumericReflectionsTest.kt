package net.apptronic.core.entity.reflection

import net.apptronic.core.BaseContextTest
import net.apptronic.core.entity.assertValueEquals
import net.apptronic.core.entity.commons.value
import org.junit.Test

class NumericReflectionsTest : BaseContextTest() {

    @Test
    fun verifyIntReflection() {
        val text = value("")
        val number = text.reflectAsInt()
        text.assertValueEquals("")
        number.assertValueEquals(null)

        number.set(1)
        text.assertValueEquals("1")
        number.assertValueEquals(1)

        number.set(100)
        text.assertValueEquals("100")
        number.assertValueEquals(100)

        text.set("512")
        text.assertValueEquals("512")
        number.assertValueEquals(512)

        text.set("")
        text.assertValueEquals("")
        number.assertValueEquals(null)

        text.set("qwerty")
        text.assertValueEquals("qwerty")
        number.assertValueEquals(null)

        number.set(-60)
        text.set("-60")
        number.assertValueEquals(-60)
    }

    @Test
    fun verifyLongReflection() {
        val text = value("")
        val number = text.reflectAsLong()
        text.assertValueEquals("")
        number.assertValueEquals(null)

        number.set(1)
        text.assertValueEquals("1")
        number.assertValueEquals(1L)

        number.set(100L)
        text.assertValueEquals("100")
        number.assertValueEquals(100L)

        text.set("512")
        text.assertValueEquals("512")
        number.assertValueEquals(512)

        text.set("")
        text.assertValueEquals("")
        number.assertValueEquals(null)

        text.set("qwerty")
        text.assertValueEquals("qwerty")
        number.assertValueEquals(null)

        number.set(-60L)
        text.set("-60")
        number.assertValueEquals(-60L)
    }

    @Test
    fun verifyFloatReflection() {
        val text = value("")
        val number = text.reflectAsFloat()
        text.assertValueEquals("")
        number.assertValueEquals(null)

        number.set(1f)
        text.assertValueEquals("1.0")
        number.assertValueEquals(1f)

        number.set(100f)
        text.assertValueEquals("100.0")
        number.assertValueEquals(100f)

        text.set("512")
        text.assertValueEquals("512")
        number.assertValueEquals(512f)

        text.set("")
        text.assertValueEquals("")
        number.assertValueEquals(null)

        text.set("qwerty")
        text.assertValueEquals("qwerty")
        number.assertValueEquals(null)

        number.set(-6.4f)
        text.set("-6.4")
        number.assertValueEquals(-6.4f)
    }

    @Test
    fun verifyDoubleReflection() {
        val text = value("")
        val number = text.reflectAsDouble()
        text.assertValueEquals("")
        number.assertValueEquals(null)

        number.set(1.0)
        text.assertValueEquals("1.0")
        number.assertValueEquals(1.0)

        number.set(100.0)
        text.assertValueEquals("100.0")
        number.assertValueEquals(100.0)

        text.set("512")
        text.assertValueEquals("512")
        number.assertValueEquals(512.0)

        text.set("")
        text.assertValueEquals("")
        number.assertValueEquals(null)

        text.set("qwerty")
        text.assertValueEquals("qwerty")
        number.assertValueEquals(null)

        number.set(-6.4)
        text.set("-6.4")
        number.assertValueEquals(-6.4)
    }

}