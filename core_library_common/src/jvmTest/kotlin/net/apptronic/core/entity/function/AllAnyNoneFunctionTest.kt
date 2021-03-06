package net.apptronic.core.entity.function

import net.apptronic.core.context.component.Component
import net.apptronic.core.entity.commons.setAs
import net.apptronic.core.entity.commons.value
import net.apptronic.core.testutils.createTestContext
import org.junit.Test

class AllAnyNoneFunctionTest {

    class TestComponent : Component(createTestContext()) {

        val val1 = value(1)
        val val2 = value(2)
        val val3 = value(3)
        val val4 = value(4)

        val result = value<Boolean>()

    }

    private val component = TestComponent()

    @Test
    fun verifyAllTrue() {
        with(component) {
            result.setAs(
                    allOf(val1, val2, val3, val4) { entity ->
                        entity.map { it > 0 }
                    }
            )
            net.apptronic.core.entity.assert(result isEqualsTo true)
        }
    }

    @Test
    fun verifyAllValuesTrue() {
        with(component) {
            result.setAs(
                    allOfValues(val1, val2, val3, val4) { value ->
                        value > 0
                    }
            )
            net.apptronic.core.entity.assert(result isEqualsTo true)
        }
    }

    @Test
    fun verifyAllFalse() {
        with(component) {
            result.setAs(
                    allOf(val1, val2, val3, val4) { entity ->
                        entity.map { it > 2 }
                    }
            )
            net.apptronic.core.entity.assert(result isEqualsTo false)
        }
    }

    @Test
    fun verifyAllValuesFalse() {
        with(component) {
            result.setAs(
                    allOfValues(val1, val2, val3, val4) { value ->
                        value > 2
                    }
            )
            net.apptronic.core.entity.assert(result isEqualsTo false)
        }
    }

    @Test
    fun verifyAnyTrue() {
        with(component) {
            result.setAs(
                    anyOf(val1, val2, val3, val4) { entity ->
                        entity.map { it > 2 }
                    }
            )
            net.apptronic.core.entity.assert(result isEqualsTo true)
        }
    }

    @Test
    fun verifyAnyValuesTrue() {
        with(component) {
            result.setAs(
                    anyOfValues(val1, val2, val3, val4) { value ->
                        value > 2
                    }
            )
            net.apptronic.core.entity.assert(result isEqualsTo true)
        }
    }

    @Test
    fun verifyAnyFalse() {
        with(component) {
            result.setAs(
                    anyOf(val1, val2, val3, val4) { entity ->
                        entity.map { it > 5 }
                    }
            )
            net.apptronic.core.entity.assert(result isEqualsTo false)
        }
    }

    @Test
    fun verifyAnyValuesFalse() {
        with(component) {
            result.setAs(
                    anyOfValues(val1, val2, val3, val4) { value ->
                        value > 5
                    }
            )
            net.apptronic.core.entity.assert(result isEqualsTo false)
        }
    }

    @Test
    fun verifyNoneTrue() {
        with(component) {
            result.setAs(
                    noneOf(val1, val2, val3, val4) { entity ->
                        entity.map { it > 5 }
                    }
            )
            net.apptronic.core.entity.assert(result isEqualsTo true)
        }
    }

    @Test
    fun verifyNoneValuesTrue() {
        with(component) {
            result.setAs(
                    noneOfValues(val1, val2, val3, val4) { value ->
                        value > 5
                    }
            )
            net.apptronic.core.entity.assert(result isEqualsTo true)
        }
    }

    @Test
    fun verifyNoneFalse() {
        with(component) {
            result.setAs(
                    noneOf(val1, val2, val3, val4) { entity ->
                        entity.map { it > 2 }
                    }
            )
            net.apptronic.core.entity.assert(result isEqualsTo false)
        }
    }

    @Test
    fun verifyNoneValuesFalse() {
        with(component) {
            result.setAs(
                    noneOfValues(val1, val2, val3, val4) { value ->
                        value > 2
                    }
            )
            net.apptronic.core.entity.assert(result isEqualsTo false)
        }
    }

}