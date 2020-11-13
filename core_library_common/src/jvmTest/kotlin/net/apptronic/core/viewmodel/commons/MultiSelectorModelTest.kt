package net.apptronic.core.viewmodel.commons

import net.apptronic.core.BaseContextTest
import net.apptronic.core.base.utils.SetEqComparator
import net.apptronic.core.entity.commons.updates
import net.apptronic.core.record
import net.apptronic.core.viewmodel.commons.SelectableValue.*
import org.junit.Test

class MultiSelectorModelTest : BaseContextTest() {

    private val selector = multiSelector<SelectableValue>()

    private val state = selector.record(SetEqComparator())
    private val updates = selector.updates.record(SetEqComparator())

    private val sw1 = selector.getSwitch(One)
    private val sw2 = selector.getSwitch(Two)
    private val sw3 = selector.getSwitch(Three)

    private val sw1state = sw1.record()
    private val sw1updates = sw1.updates.record()
    private val sw2state = sw2.record()
    private val sw2updates = sw2.updates.record()
    private val sw3state = sw3.record()
    private val sw3updates = sw3.updates.record()

    private fun clear() {
        state.clear()
        updates.clear()
        sw1state.clear()
        sw1updates.clear()
        sw2state.clear()
        sw2updates.clear()
        sw3state.clear()
        sw3updates.clear()
    }

    @Test
    fun verify() {
        state.assertItems(emptySet())
        sw1state.assertItems(false)
        sw1updates.assertItems()
        sw2state.assertItems(false)
        sw2updates.assertItems()
        sw3state.assertItems(false)
        sw3updates.assertItems()
        clear()

        selector.set(One, Two)
        state.assertItems(setOf(One, Two))
        sw1state.assertItems(true)
        sw1updates.assertItems()
        sw2state.assertItems(true)
        sw2updates.assertItems()
        sw3state.assertItems()
        sw3updates.assertItems()
        clear()

        selector.update(One, Four)
        state.assertItems(setOf(One, Four))
        sw1state.assertItems()
        sw1updates.assertItems()
        sw2state.assertItems(false)
        sw2updates.assertItems(false)
        sw3state.assertItems()
        sw3updates.assertItems()
        clear()

        selector.set(Two)
        state.assertItems(setOf(Two))
        sw1state.assertItems(false)
        sw1updates.assertItems()
        sw2state.assertItems(true)
        sw2updates.assertItems()
        sw3state.assertItems()
        sw3updates.assertItems()
        clear()
    }

}