package net.apptronic.core.component.entity.behavior

import net.apptronic.core.component.entity.entities.distinctUntilChanged
import net.apptronic.core.component.typedEvent
import net.apptronic.core.component.value
import net.apptronic.core.record
import net.apptronic.core.testutils.testContext
import org.junit.Test
import kotlin.test.assertEquals

class BooleanBehaviorExtensionsTest {

    val context = testContext()

    @Test
    fun verifyWhenTrue() {
        val source = context.typedEvent<Boolean>()
        val result = source.whenTrue().record()

        source.sendEvent(false)
        result.assertItems()
        source.sendEvent(true)
        result.assertItems(Unit)
        source.sendEvent(false)
        result.assertItems(Unit)
        source.sendEvent(true)
        result.assertItems(Unit, Unit)
    }

    @Test
    fun verifyWhenFalse() {
        val source = context.typedEvent<Boolean>()
        val result = source.whenFalse().record()

        source.sendEvent(false)
        result.assertItems(Unit)
        source.sendEvent(true)
        result.assertItems(Unit)
        source.sendEvent(false)
        result.assertItems(Unit, Unit)
        source.sendEvent(true)
        result.assertItems(Unit, Unit)
    }

    @Test
    fun verifyWhenTrueNotNull() {
        val source = context.typedEvent<Boolean?>()
        val result = source.whenTrueNotNull().record()

        source.sendEvent(false)
        result.assertItems()
        source.sendEvent(true)
        result.assertItems(Unit)
        source.sendEvent(null)
        result.assertItems(Unit)
        source.sendEvent(true)
        result.assertItems(Unit, Unit)
    }

    @Test
    fun verifyWhenFalseNotNull() {
        val source = context.typedEvent<Boolean?>()
        val result = source.whenFalseNotNull().record()

        source.sendEvent(false)
        result.assertItems(Unit)
        source.sendEvent(true)
        result.assertItems(Unit)
        source.sendEvent(null)
        result.assertItems(Unit)
        source.sendEvent(true)
        result.assertItems(Unit)
    }

    @Test
    fun verifyWhenTrueOrNull() {
        val source = context.typedEvent<Boolean?>()
        val result = source.whenTrueOrNull().record()

        source.sendEvent(false)
        result.assertItems()
        source.sendEvent(true)
        result.assertItems(true)
        source.sendEvent(null)
        result.assertItems(true, null)
        source.sendEvent(true)
        result.assertItems(true, null, true)
    }

    @Test
    fun verifyWhenFalseOrNull() {
        val source = context.typedEvent<Boolean?>()
        val result = source.whenFalseOrNull().record()

        source.sendEvent(false)
        result.assertItems(false)
        source.sendEvent(true)
        result.assertItems(false)
        source.sendEvent(null)
        result.assertItems(false, null)
        source.sendEvent(true)
        result.assertItems(false, null)
    }

    @Test
    fun verifyDoWhenTrue() {
        val source = context.typedEvent<Boolean>()
        var calls = 0
        source.doWhenTrue {
            calls++
        }

        source.sendEvent(false)
        assertEquals(calls, 0)

        source.sendEvent(true)
        assertEquals(calls, 1)

        source.sendEvent(false)
        source.sendEvent(false)
        source.sendEvent(false)
        assertEquals(calls, 1)

        source.sendEvent(true)
        source.sendEvent(true)
        source.sendEvent(true)
        assertEquals(calls, 4)
    }

    @Test
    fun verifyDoWhenFalse() {
        val source = context.typedEvent<Boolean>()
        var calls = 0
        source.doWhenFalse {
            calls++
        }

        source.sendEvent(false)
        assertEquals(calls, 1)

        source.sendEvent(true)
        assertEquals(calls, 1)

        source.sendEvent(false)
        source.sendEvent(false)
        source.sendEvent(false)
        assertEquals(calls, 4)

        source.sendEvent(true)
        source.sendEvent(true)
        source.sendEvent(true)
        assertEquals(calls, 4)
    }

    @Test
    fun verifyWhenever() {
        val source = context.typedEvent<Boolean>()
        var calls = 0
        context.whenever(source) {
            calls++
        }

        source.sendEvent(false)
        assertEquals(calls, 0)

        source.sendEvent(true)
        assertEquals(calls, 1)

        source.sendEvent(false)
        source.sendEvent(false)
        source.sendEvent(false)
        assertEquals(calls, 1)

        source.sendEvent(true)
        source.sendEvent(true)
        source.sendEvent(true)
        assertEquals(calls, 4)
    }

    @Test
    fun verifyWheneverNot() {
        val source = context.typedEvent<Boolean>()
        var calls = 0
        context.wheneverNot(source) {
            calls++
        }

        source.sendEvent(false)
        assertEquals(calls, 1)

        source.sendEvent(true)
        assertEquals(calls, 1)

        source.sendEvent(false)
        source.sendEvent(false)
        source.sendEvent(false)
        assertEquals(calls, 4)

        source.sendEvent(true)
        source.sendEvent(true)
        source.sendEvent(true)
        assertEquals(calls, 4)
    }

    @Test
    fun verifySelectIfEntityEmpty() {
        val state = context.value<Boolean>()
        val ifTrue = context.value<String>()

        val result = state.selectIf(ifTrue).record()
        state.set(false)
        result.assertItems()
        state.set(true)
        result.assertItems()
    }

    @Test
    fun verifySelectIfEntityStateEmpty() {
        val state = context.value<Boolean>()
        val ifTrue = context.value<String>()

        val result = state.selectIf(ifTrue).record()
        ifTrue.set("ifTrue")
        result.assertItems()
    }

    @Test
    fun verifySelectIfEntity() {
        val state = context.value<Boolean>()
        val ifTrue = context.value<String>()

        val result = state.selectIf(ifTrue).record()

        state.set(false)
        ifTrue.set("true-1")
        result.assertItems()

        state.set(true)
        result.assertItems("true-1")

        ifTrue.set("true-2")
        result.assertItems("true-1", "true-2")

        state.set(false)
        ifTrue.set("true-3")
        ifTrue.set("true-4")
        ifTrue.set("true-5")
        result.assertItems("true-1", "true-2")

        state.set(true)
        result.assertItems("true-1", "true-2", "true-5")
    }

    @Test
    fun verifySelectIfEntitiesEmpty() {
        val state = context.value<Boolean>()
        val ifTrue = context.value<String>()
        val ifFalse = context.value<String>()

        val result = state.selectIf(ifTrue, ifFalse).record()
        state.set(false)
        result.assertItems()
        state.set(true)
        result.assertItems()
    }

    @Test
    fun verifySelectIfEntitiesStateEmpty() {
        val state = context.value<Boolean>()
        val ifTrue = context.value<String>()
        val ifFalse = context.value<String>()

        val result = state.selectIf(ifTrue, ifFalse).record()
        ifTrue.set("ifTrue")
        ifFalse.set("ifFalse")
        result.assertItems()
    }

    @Test
    fun verifySelectIfEntitiesOnlyTrue() {
        val state = context.value<Boolean>()
        val ifTrue = context.value<String>()
        val ifFalse = context.value<String>()

        val result = state.selectIf(ifTrue, ifFalse).record()
        state.set(true)
        ifTrue.set("TrueValue")
        result.assertItems("TrueValue")
    }

    @Test
    fun verifySelectIfEntitiesOnlyFalse() {
        val state = context.value<Boolean>()
        val ifTrue = context.value<String>()
        val ifFalse = context.value<String>()

        val result = state.selectIf(ifTrue, ifFalse).record()
        state.set(false)
        ifFalse.set("FalseValue")
        result.assertItems("FalseValue")
    }

    @Test
    fun verifySelectIfEntities() {
        val state = context.value<Boolean>()
        val ifTrue = context.value<String>()
        val ifFalse = context.value<String>()

        val result = state.selectIf(ifTrue, ifFalse).distinctUntilChanged().record()

        ifTrue.set("true-1")
        ifFalse.set("false-1")
        state.set(true)

        result.assertItems("true-1")

        state.set(false)
        result.assertItems("true-1", "false-1")

        ifTrue.set("true-2")
        result.assertItems("true-1", "false-1")

        ifFalse.set("false-2")
        result.assertItems("true-1", "false-1", "false-2")

        state.set(true)
        result.assertItems("true-1", "false-1", "false-2", "true-2")
    }

    @Test
    fun verifySelectIfTrueEntityFalseValueNotSet() {
        val state = context.value<Boolean>()
        val ifTrue = context.value<String>()
        val result = state.selectIf(ifTrue, "ifFalse").distinctUntilChanged().record()

        state.set(true)
        result.assertItems()
        state.set(false)
        result.assertItems("ifFalse")
    }

    @Test
    fun verifySelectIfTrueEntityFalseValueStateNotSet() {
        val state = context.value<Boolean>()
        val ifTrue = context.value<String>()
        val result = state.selectIf(ifTrue, "ifFalse").distinctUntilChanged().record()

        ifTrue.set("ifTrue")
        result.assertItems()
    }

    @Test
    fun verifySelectIfTrueEntityFalseValue() {
        val state = context.value<Boolean>()
        val ifTrue = context.value<String>()
        val result = state.selectIf(ifTrue, "ifFalse").distinctUntilChanged().record()

        ifTrue.set("true-1")
        state.set(true)
        result.assertItems("true-1")

        ifTrue.set("true-2")
        result.assertItems("true-1", "true-2")

        state.set(false)
        result.assertItems("true-1", "true-2", "ifFalse")

        ifTrue.set("true-3")
        ifTrue.set("true-4")
        ifTrue.set("true-5")
        ifTrue.set("true-6")
        result.assertItems("true-1", "true-2", "ifFalse")

        state.set(true)
        result.assertItems("true-1", "true-2", "ifFalse", "true-6")
    }

    @Test
    fun verifySelectIfTrueValueFalseFantityValueNotSet() {
        val state = context.value<Boolean>()
        val ifFalse = context.value<String>()
        val result = state.selectIf("ifTrue", ifFalse).distinctUntilChanged().record()

        state.set(true)
        result.assertItems("ifTrue")
        state.set(false)
        result.assertItems("ifTrue")
    }

    @Test
    fun verifySelectIfTrueValueFalseEntityStateNotSet() {
        val state = context.value<Boolean>()
        val ifFalse = context.value<String>()
        val result = state.selectIf("ifTrue", ifFalse).distinctUntilChanged().record()

        ifFalse.set("ifFalse")
        result.assertItems()
    }

    @Test
    fun verifySelectIfTrueValueFalseEntity() {
        val state = context.value<Boolean>()
        val ifFalse = context.value<String>()
        val result = state.selectIf("ifTrue", ifFalse).distinctUntilChanged().record()

        ifFalse.set("false-1")
        state.set(false)
        result.assertItems("false-1")

        ifFalse.set("false-2")
        result.assertItems("false-1", "false-2")

        state.set(true)
        result.assertItems("false-1", "false-2", "ifTrue")

        ifFalse.set("false-3")
        ifFalse.set("false-4")
        ifFalse.set("false-5")
        ifFalse.set("false-6")
        result.assertItems("false-1", "false-2", "ifTrue")

        state.set(false)
        result.assertItems("false-1", "false-2", "ifTrue", "false-6")
    }

    @Test
    fun verifyWhenAnyValue() {
        val source = context.value<String>()
        val result = source.whenAnyValue().distinctUntilChanged().record()

        result.assertItems(false)
        source.set("One")
        result.assertItems(false, true)
        source.set("Two")
        source.set("Three")
        source.set("Four")
        result.assertItems(false, true)
    }

    @Test
    fun verifyWhenAny() {
        val source = context.value<String>()
        val result = source.whenAny {
            it.startsWith("match")
        }.distinctUntilChanged().record()

        result.assertItems(false)
        source.set("not_match_1")
        result.assertItems(false)
        source.set("match_1")
        result.assertItems(false, true)
        source.set("match_2")
        source.set("match_3")
        result.assertItems(false, true)
        source.set("not_match_2")
        result.assertItems(false, true)
        source.set("not_match_3")
        source.set("not_match_4")
        source.set("match_5")
        source.set("match_6")
        source.set("match_7")
        result.assertItems(false, true)
    }

    @Test
    fun verifyWhenAnySuspend() {
        val source = context.value<String>()
        val result = source.whenAnySuspend {
            it.startsWith("match")
        }.distinctUntilChanged().record()

        result.assertItems(false)
        source.set("not_match_1")
        result.assertItems(false)
        source.set("match_1")
        result.assertItems(false, true)
        source.set("match_2")
        source.set("match_3")
        result.assertItems(false, true)
        source.set("not_match_2")
        result.assertItems(false, true)
        source.set("not_match_3")
        source.set("not_match_4")
        source.set("match_5")
        source.set("match_6")
        source.set("match_7")
        result.assertItems(false, true)
    }

}