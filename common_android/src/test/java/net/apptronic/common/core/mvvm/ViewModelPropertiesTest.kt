package net.apptronic.common.core.mvvm

import net.apptronic.common.core.mvvm.threading.SynchronousExecutor
import net.apptronic.common.core.mvvm.viewmodel.ViewModel
import net.apptronic.common.core.mvvm.viewmodel.entity.PropertyNotSetException
import net.apptronic.common.core.mvvm.viewmodel.extensions.asFunctionOf
import net.apptronic.common.core.mvvm.viewmodel.extensions.copyValueFrom
import net.apptronic.common.core.mvvm.viewmodel.lifecycle.GenericLifecycle
import net.apptronic.common.core.mvvm.viewmodel.lifecycle.Lifecycle
import org.junit.Test
import kotlin.test.assertFailsWith

class ViewModelPropertiesTest {

    private val lifecycle = GenericLifecycle(SynchronousExecutor())

    private class SampleViewModel(lifecycle: Lifecycle) : ViewModel(lifecycle) {

        val stringValue = value<String>()

        val stringValueWithDefault = value<String>("Default")

        val intValue = value<Int>()

        val compositeValue = value<String>().asFunctionOf(
            stringValue,
            stringValueWithDefault
        ) { str1, str2 ->
            "$str1-$str2"
        }

        val compositeStringInt = value<String>().asFunctionOf(
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

    @Test

    fun shouldAutoUnsubscribeCompletely() {
        var stringValue: String? = null
        val model = SampleViewModel(lifecycle)

        lifecycle.stage1.enter()
        model.stringValue.subscribe { stringValue = it }
        model.stringValue.set("One")
        assert(stringValue == "One")

        lifecycle.stage1.exit()
        model.stringValue.set("Two")
        assert(stringValue == "One")

        lifecycle.stage1.enter()
        model.stringValue.set("Three")
        assert(stringValue == "One")
    }

    @Test
    fun shouldAutoUnsibscribeAndResubscribe() {
        var stringValue: String? = null
        val model = SampleViewModel(lifecycle)

        lifecycle.stage1.enter()

        lifecycle.stage2.doOnEnter {
            model.stringValue.subscribe { stringValue = it }
        }

        lifecycle.stage2.enter()
        model.stringValue.set("One")
        assert(stringValue == "One")

        lifecycle.stage2.exit()
        model.stringValue.set("Two")
        assert(stringValue == "One")

        lifecycle.stage2.enter()
        assert(stringValue == "Two")
    }

    @Test
    fun shouldAutoAssignAfterSubscribe() {
        var stringValue: String? = null
        val model = SampleViewModel(lifecycle)
        model.stringValue.set("One")

        lifecycle.stage1.enter()
        model.stringValue.subscribe { stringValue = it }
        assert(stringValue == "One")
    }

    @Test
    fun shouldAutoFireOnStageEntered() {
        var called = false

        lifecycle.stage1.doOnEnter {
            called = true
        }

        lifecycle.stage1.enter()
        assert(called)
    }

    @Test
    fun functionShouldBeCalculatedWhenAllIsSet() {
        var compositeValue: String? = null
        var compositeStringInt: String? = null
        val model = SampleViewModel(lifecycle)
        lifecycle.stage1.enter()
        model.compositeValue.subscribe { compositeValue = it }
        model.compositeStringInt.subscribe { compositeStringInt = it }
        assert(compositeValue == null)
        assert(compositeStringInt == null)
        assertFailsWith<PropertyNotSetException> { model.compositeValue.get() }
        assertFailsWith<PropertyNotSetException> { model.compositeStringInt.get() }

        model.stringValue.set("Changed")
        assert(compositeValue == "Changed-Default")

        model.stringValueWithDefault.set("Another")
        assert(compositeValue == "Changed-Another")

        assert(compositeStringInt == null)
        assertFailsWith<PropertyNotSetException> { model.compositeStringInt.get() }

        model.intValue.set(1)
        assert(compositeStringInt == "Changed-1")

        model.stringValue.set("ChangedAgain")
        assert(compositeStringInt == "ChangedAgain-1")

        model.intValue.set(2)
        assert(compositeStringInt == "ChangedAgain-2")
    }

    @Test
    fun shouldCopy() {
        val model = SampleViewModel(lifecycle)

        assert(model.copyOfString.get() == "Default")
        assertFailsWith<PropertyNotSetException> { model.copyOfInt.get() }

        model.intValue.set(1)
        assert(model.copyOfInt.get() == 1)

        model.intValue.set(2)
        model.intValue.set(3)
        assert(model.copyOfInt.get() == 3)

        model.copyOfString.set("Other")
        assert(model.copyOfString.get() == "Other")

    }

    @Test
    fun checkNullables() {
        val model = SampleViewModel(lifecycle)
        lifecycle.stage1.enter()

        assertFailsWith<PropertyNotSetException> { model.nullableString.get() }
        assert(model.nullableStringDefaultNull.get() == null)

        model.nullableStringDefaultNull.set("Value")
        assert(model.nullableStringDefaultNull.get() == "Value")

        model.nullableStringDefaultNull.set(null)
        assert(model.nullableStringDefaultNull.get() == null)

        var calls = 0
        model.nullableString.subscribe {
            calls++
            if (calls == 1) {
                assert(it == "Some")
            }
            if (calls == 2) {
                assert(it == null)
            }
        }
        assert(calls == 0)

        model.nullableString.set("Some")
        assert(calls == 1)
        assert(model.nullableString.get() == "Some")

        model.nullableString.set(null)
        assert(calls == 2)
        assert(model.nullableString.get() == null)

    }

}