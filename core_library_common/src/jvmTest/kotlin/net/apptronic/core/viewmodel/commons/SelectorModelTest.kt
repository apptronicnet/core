package net.apptronic.core.viewmodel.commons

import net.apptronic.core.BaseContextTest
import net.apptronic.core.EntityRecorder
import net.apptronic.core.entity.base.MutableValue
import net.apptronic.core.record
import net.apptronic.core.viewmodel.commons.SelectableValue.*
import org.junit.Test
import kotlin.test.assertEquals

class SelectorModelTest : BaseContextTest() {

    private lateinit var selector: SelectorModel<SelectableValue>

    private lateinit var state: EntityRecorder<SelectableValue?>
    private lateinit var updates: EntityRecorder<SelectableValue?>

    private lateinit var sw1: MutableValue<Boolean>
    private lateinit var sw2: MutableValue<Boolean>
    private lateinit var sw3: MutableValue<Boolean>

    private lateinit var sw1state: EntityRecorder<Boolean>
    private lateinit var sw1updates: EntityRecorder<Boolean>
    private lateinit var sw2state: EntityRecorder<Boolean>
    private lateinit var sw2updates: EntityRecorder<Boolean>
    private lateinit var sw3state: EntityRecorder<Boolean>
    private lateinit var sw3updates: EntityRecorder<Boolean>

    private fun assertState(current: SelectableValue?) {
        assertEquals(selector.get(), current)
        assertEquals(selector.get() == One, sw1.get())
        assertEquals(selector.get() == Two, sw2.get())
        assertEquals(selector.get() == Three, sw3.get())
    }

    private fun init(supportsUnselect: Boolean) {
        selector = selector(null, supportsUnselect)
        state = selector.record()
        updates = selector.updates.record()

        sw1 = selector.getSwitch(One)
        sw2 = selector.getSwitch(Two)
        sw3 = selector.getSwitch(Three)

        sw1state = sw1.record()
        sw1updates = sw1.updates.record()
        sw2state = sw2.record()
        sw2updates = sw2.updates.record()
        sw3state = sw3.record()
        sw3updates = sw3.updates.record()

        assertState(null)
        state.assertItems(null)
        updates.assertItems()
        sw1state.assertItems(false)
        sw1updates.assertItems()
        sw2state.assertItems(false)
        sw2updates.assertItems()
        sw3state.assertItems(false)
        sw3updates.assertItems()
    }

    @Test
    fun verifySupportsUnselect() {
        init(true)

        sw1.set(true)
        assertState(One)
        state.assertItems(null, One)
        updates.assertItems()
        sw1state.assertItems(false, true)
        sw1updates.assertItems()
        sw2state.assertItems(false)
        sw2updates.assertItems()
        sw3state.assertItems(false)
        sw3updates.assertItems()

        sw1.set(false)
        assertState(null)
        state.assertItems(null, One, null)
        updates.assertItems()
        sw1state.assertItems(false, true, false)
        sw1updates.assertItems()
        sw2state.assertItems(false)
        sw2updates.assertItems()
        sw3state.assertItems(false)
        sw3updates.assertItems()

        sw2.update(true)
        assertState(Two)
        state.assertItems(null, One, null, Two)
        updates.assertItems(Two)
        sw1state.assertItems(false, true, false)
        sw1updates.assertItems()
        sw2state.assertItems(false, true)
        sw2updates.assertItems(true)
        sw3state.assertItems(false)
        sw3updates.assertItems()

        sw3.update(true)
        assertState(Three)
        state.assertItems(null, One, null, Two, Three)
        updates.assertItems(Two, Three)
        sw1state.assertItems(false, true, false)
        sw1updates.assertItems()
        sw2state.assertItems(false, true, false)
        sw2updates.assertItems(true, false)
        sw3state.assertItems(false, true)
        sw3updates.assertItems(true)

        sw3.update(false)
        assertState(null)
        state.assertItems(null, One, null, Two, Three, null)
        updates.assertItems(Two, Three, null)
        sw1state.assertItems(false, true, false)
        sw1updates.assertItems()
        sw2state.assertItems(false, true, false)
        sw2updates.assertItems(true, false)
        sw3state.assertItems(false, true, false)
        sw3updates.assertItems(true, false)

        selector.update(One)
        assertState(One)
        state.assertItems(null, One, null, Two, Three, null, One)
        updates.assertItems(Two, Three, null, One)
        sw1state.assertItems(false, true, false, true)
        sw1updates.assertItems(true)
        sw2state.assertItems(false, true, false)
        sw2updates.assertItems(true, false)
        sw3state.assertItems(false, true, false)
        sw3updates.assertItems(true, false)

        selector.set(Two)
        assertState(Two)
        state.assertItems(null, One, null, Two, Three, null, One, Two)
        updates.assertItems(Two, Three, null, One)
        sw1state.assertItems(false, true, false, true, false)
        sw1updates.assertItems(true)
        sw2state.assertItems(false, true, false, true)
        sw2updates.assertItems(true, false)
        sw3state.assertItems(false, true, false)
        sw3updates.assertItems(true, false)

        selector.update(null)
        assertState(null)
        state.assertItems(null, One, null, Two, Three, null, One, Two, null)
        updates.assertItems(Two, Three, null, One, null)
        sw1state.assertItems(false, true, false, true, false)
        sw1updates.assertItems(true)
        sw2state.assertItems(false, true, false, true, false)
        sw2updates.assertItems(true, false, false)
        sw3state.assertItems(false, true, false)
        sw3updates.assertItems(true, false)
    }

    @Test
    fun verifyNotSupportsUnselect() {
        init(false)

        sw1.set(true)
        assertState(One)
        state.assertItems(null, One)
        updates.assertItems()
        sw1state.assertItems(false, true)
        sw1updates.assertItems()
        sw2state.assertItems(false)
        sw2updates.assertItems()
        sw3state.assertItems(false)
        sw3updates.assertItems()

        sw1.set(false)
        assertState(One)
        state.assertItems(null, One)
        updates.assertItems()
        sw1state.assertItems(false, true, false, true)
        sw1updates.assertItems()
        sw2state.assertItems(false)
        sw2updates.assertItems()
        sw3state.assertItems(false)
        sw3updates.assertItems()

        sw2.update(true)
        assertState(Two)
        state.assertItems(null, One, Two)
        updates.assertItems(Two)
        sw1state.assertItems(false, true, false, true, false)
        sw1updates.assertItems(false)
        sw2state.assertItems(false, true)
        sw2updates.assertItems(true)
        sw3state.assertItems(false)
        sw3updates.assertItems()

        sw3.update(true)
        assertState(Three)
        state.assertItems(null, One, Two, Three)
        updates.assertItems(Two, Three)
        sw1state.assertItems(false, true, false, true, false)
        sw1updates.assertItems(false)
        sw2state.assertItems(false, true, false)
        sw2updates.assertItems(true, false)
        sw3state.assertItems(false, true)
        sw3updates.assertItems(true)

        sw3.update(false)
        assertState(Three)
        state.assertItems(null, One, Two, Three)
        updates.assertItems(Two, Three)
        sw1state.assertItems(false, true, false, true, false)
        sw1updates.assertItems(false)
        sw2state.assertItems(false, true, false)
        sw2updates.assertItems(true, false)
        sw3state.assertItems(false, true, false, true)
        sw3updates.assertItems(true)

        selector.update(One)
        assertState(One)
        state.assertItems(null, One, Two, Three, One)
        updates.assertItems(Two, Three, One)
        sw1state.assertItems(false, true, false, true, false, true)
        sw1updates.assertItems(false, true)
        sw2state.assertItems(false, true, false)
        sw2updates.assertItems(true, false)
        sw3state.assertItems(false, true, false, true, false)
        sw3updates.assertItems(true, false)

        selector.set(Two)
        assertState(Two)
        state.assertItems(null, One, Two, Three, One, Two)
        updates.assertItems(Two, Three, One)
        sw1state.assertItems(false, true, false, true, false, true, false)
        sw1updates.assertItems(false, true)
        sw2state.assertItems(false, true, false, true)
        sw2updates.assertItems(true, false)
        sw3state.assertItems(false, true, false, true, false)
        sw3updates.assertItems(true, false)

        selector.update(null)
        assertState(null)
        state.assertItems(null, One, Two, Three, One, Two, null)
        updates.assertItems(Two, Three, One, null)
        sw1state.assertItems(false, true, false, true, false, true, false)
        sw1updates.assertItems(false, true)
        sw2state.assertItems(false, true, false, true, false)
        sw2updates.assertItems(true, false, false)
        sw3state.assertItems(false, true, false, true, false)
        sw3updates.assertItems(true, false)
    }

}