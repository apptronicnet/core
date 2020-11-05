package net.apptronic.core.entity.function

import net.apptronic.core.entity.commons.Property
import net.apptronic.core.entity.commons.Value
import net.apptronic.core.entity.commons.asProperty
import net.apptronic.core.entity.commons.value
import net.apptronic.core.testutils.BaseTestComponent
import org.junit.Test
import kotlin.test.assertEquals

class NumericFunctionsTest {

    private abstract class TestComponent<T : Number> : BaseTestComponent() {

        val left = value<T>()
        val right = value<T>()

        val sum = (left + right).asProperty()
        val subs = (left - right).asProperty()
        val mult = (left * right).asProperty()
        val div = (left / right).asProperty()

        abstract fun set(target: Value<T>, value: Int)
        abstract fun assert(target: Property<T>, expected: Int)

    }

    @Test
    fun verifyByte() {
        verify(object : TestComponent<Byte>() {
            override fun set(target: Value<Byte>, value: Int) = target.set(value.toByte())
            override fun assert(target: Property<Byte>, expected: Int) = assertEquals(target.get(), expected.toByte())
        })
    }

    @Test
    fun verifyShort() {
        verify(object : TestComponent<Short>() {
            override fun set(target: Value<Short>, value: Int) = target.set(value.toShort())
            override fun assert(target: Property<Short>, expected: Int) = assertEquals(target.get(), expected.toShort())
        })
    }

    @Test
    fun verifyInt() {
        verify(object : TestComponent<Int>() {
            override fun set(target: Value<Int>, value: Int) = target.set(value.toInt())
            override fun assert(target: Property<Int>, expected: Int) = assertEquals(target.get(), expected.toInt())
        })
    }

    @Test
    fun verifyLong() {
        verify(object : TestComponent<Long>() {
            override fun set(target: Value<Long>, value: Int) = target.set(value.toLong())
            override fun assert(target: Property<Long>, expected: Int) = assertEquals(target.get(), expected.toLong())
        })
    }

    @Test
    fun verifyFloat() {
        verify(object : TestComponent<Float>() {
            override fun set(target: Value<Float>, value: Int) = target.set(value.toFloat())
            override fun assert(target: Property<Float>, expected: Int) = assertEquals(target.get(), expected.toFloat())
        })
    }

    @Test
    fun verifyDouble() {
        verify(object : TestComponent<Double>() {
            override fun set(target: Value<Double>, value: Int) = target.set(value.toDouble())
            override fun assert(target: Property<Double>, expected: Int) = assertEquals(target.get(), expected.toDouble())
        })
    }

    private fun <T : Number> verify(component: TestComponent<T>) {
        component.set(component.left, 12)
        component.set(component.right, 4)
        component.assert(component.sum, 16)
        component.assert(component.subs, 8)
        component.assert(component.mult, 48)
        component.assert(component.div, 3)
    }

}