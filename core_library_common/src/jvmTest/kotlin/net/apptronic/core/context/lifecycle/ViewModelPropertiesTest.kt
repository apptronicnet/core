package net.apptronic.core.context.lifecycle

import net.apptronic.core.entity.ValueNotSetException
import net.apptronic.core.entity.value
import net.apptronic.core.testutils.BaseTestComponent
import net.apptronic.core.testutils.TestLifecycle
import net.apptronic.core.viewmodel.extensions.copyValueFrom
import net.apptronic.core.viewmodel.extensions.functionOf
import org.junit.Test
import kotlin.test.assertFailsWith

class ViewModelPropertiesTest {

    private class TestComponent : BaseTestComponent() {

        val stringValue = value<String>()

        val stringValueWithDefault = value<String>("Default")

        val intValue = value<Int>()

        val compositeValue = functionOf(
            stringValue,
            stringValueWithDefault
        ) { str1, str2 ->
            "$str1-$str2"
        }

        val compositeStringInt = functionOf(
            stringValue,
            intValue
        ) { str1, intVal ->
            "$str1-$intVal"
        }

        val copyOfString = value<String>().copyValueFrom(stringValueWithDefault)

        val copyOfInt = value<Int>().copyValueFrom(intValue)

        val nullableString = value<String?>()

        val nullableStringDefaultNull = value<String?>(null)

    }

    private val component = TestComponent()
    private val lifecycle = component.getLifecycle()

    @Test
    fun shouldAutoUnsubscribeCompletely() {
        var stringValue: String? = null

        lifecycle.enterStage(TestLifecycle.STAGE_CREATED)
        component.stringValue.subscribe { stringValue = it }
        component.stringValue.set("One")
        assert(stringValue == "One")

        lifecycle.exitStage(TestLifecycle.STAGE_CREATED)
        component.stringValue.set("Two")
        assert(stringValue == "One")

        lifecycle.enterStage(TestLifecycle.STAGE_CREATED)
        component.stringValue.set("Three")
        assert(stringValue == "One")
    }

    @Test
    fun shouldAutoUnsibscribeAndResubscribe() {
        var stringValue: String? = null

        lifecycle.enterStage(TestLifecycle.STAGE_CREATED)

        component.doOnActivated {
            component.stringValue.subscribe { stringValue = it }
        }

        lifecycle.enterStage(TestLifecycle.STAGE_ACTIVATED)
        component.stringValue.set("One")
        assert(stringValue == "One")

        lifecycle.exitStage(TestLifecycle.STAGE_ACTIVATED)
        component.stringValue.set("Two")
        assert(stringValue == "One")

        lifecycle.enterStage(TestLifecycle.STAGE_ACTIVATED)
        assert(stringValue == "Two")
    }

    @Test
    fun shouldAutoAssignAfterSubscribe() {
        var stringValue: String? = null
        component.stringValue.set("One")

        lifecycle.enterStage(TestLifecycle.STAGE_CREATED)
        component.stringValue.subscribe { stringValue = it }
        assert(stringValue == "One")
    }

    @Test
    fun shouldAutoFireOnStageEntered() {
        var called = false

        component.doOnCreate {
            called = true
        }

        lifecycle.enterStage(TestLifecycle.STAGE_CREATED)
        assert(called)
    }

    @Test
    fun functionShouldBeCalculatedWhenAllIsSet() {
        var compositeValue: String? = null
        var compositeStringInt: String? = null
        lifecycle.enterStage(TestLifecycle.STAGE_CREATED)
        component.compositeValue.subscribe { compositeValue = it }
        component.compositeStringInt.subscribe { compositeStringInt = it }
        assert(compositeValue == null)
        assert(compositeStringInt == null)
        assertFailsWith<ValueNotSetException> {
            component.compositeValue.get()
        }
        assertFailsWith<ValueNotSetException> {
            component.compositeStringInt.get()
        }

        component.stringValue.set("Changed")
        assert(compositeValue == "Changed-Default")

        component.stringValueWithDefault.set("Another")
        assert(compositeValue == "Changed-Another")

        assert(compositeStringInt == null)
        assertFailsWith<ValueNotSetException> { component.compositeStringInt.get() }

        component.intValue.set(1)
        assert(compositeStringInt == "Changed-1")

        component.stringValue.set("ChangedAgain")
        assert(compositeStringInt == "ChangedAgain-1")

        component.intValue.set(2)
        assert(compositeStringInt == "ChangedAgain-2")
    }

    @Test
    fun shouldCopy() {
        assert(component.copyOfString.get() == "Default")
        assertFailsWith<ValueNotSetException> { component.copyOfInt.get() }

        component.intValue.set(1)
        assert(component.copyOfInt.get() == 1)

        component.intValue.set(2)
        component.intValue.set(3)
        assert(component.copyOfInt.get() == 3)

        component.copyOfString.set("Other")
        assert(component.copyOfString.get() == "Other")

    }

    @Test
    fun checkNullables() {
        lifecycle.enterStage(TestLifecycle.STAGE_CREATED)

        assertFailsWith<ValueNotSetException> { component.nullableString.get() }
        assert(component.nullableStringDefaultNull.get() == null)

        component.nullableStringDefaultNull.set("Value")
        assert(component.nullableStringDefaultNull.get() == "Value")

        component.nullableStringDefaultNull.set(null)
        assert(component.nullableStringDefaultNull.get() == null)

        var calls = 0
        component.nullableString.subscribe {
            calls++
            if (calls == 1) {
                assert(it == "Some")
            }
            if (calls == 2) {
                assert(it == null)
            }
        }
        assert(calls == 0)

        component.nullableString.set("Some")
        assert(calls == 1)
        assert(component.nullableString.get() == "Some")

        component.nullableString.set(null)
        assert(calls == 2)
        assert(component.nullableString.get() == null)

    }

}