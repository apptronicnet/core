package net.apptronic.core.viewmodel.commons

import net.apptronic.core.BaseContextTest
import net.apptronic.core.entity.commons.updates
import net.apptronic.core.record
import net.apptronic.core.viewmodel.commons.SelectableValue.*
import org.junit.Test

class ListSelectorModelTest : BaseContextTest() {

    private val selector = listSelector<SelectableValue, Int> { it.id }
    private val state = selector.record()
    private val idState = selector.id.record()
    private val updates = selector.updates.record()
    private val idUpdates = selector.id.updates.record()

    @Test
    fun verifyCantSetNotInItems() {
        selector.set(Four)

        state.assertItems(null)
        idState.assertItems(null)
        updates.assertItems()
        idUpdates.assertItems()
    }

    @Test
    fun verifyCantSetNotInItemsWhenOtherItemsSet() {
        selector.items.set(listOf(One, Two, Three))
        selector.set(Four)

        state.assertItems(null)
        idState.assertItems(null)
        updates.assertItems()
        idUpdates.assertItems()
    }

    @Test
    fun verifyCanSetAndUpdate() {
        selector.items.set(values().toList())
        selector.set(Four)

        state.assertItems(null, Four)
        idState.assertItems(null, 4)
        updates.assertItems()
        idUpdates.assertItems()

        selector.update(Five)

        state.assertItems(null, Four, Five)
        idState.assertItems(null, 4, 5)
        updates.assertItems(Five)
        idUpdates.assertItems(5)

        // set to same value
        selector.set(Five)

        state.assertItems(null, Four, Five)
        idState.assertItems(null, 4, 5)
        updates.assertItems(Five)
        idUpdates.assertItems(5)

        // update to same value
        selector.set(Five)

        state.assertItems(null, Four, Five)
        idState.assertItems(null, 4, 5)
        updates.assertItems(Five)
        idUpdates.assertItems(5)

        selector.set(Two)

        state.assertItems(null, Four, Five, Two)
        idState.assertItems(null, 4, 5, 2)
        updates.assertItems(Five)
        idUpdates.assertItems(5)

        // update to same value
        selector.update(Two)

        state.assertItems(null, Four, Five, Two)
        idState.assertItems(null, 4, 5, 2)
        updates.assertItems(Five)
        idUpdates.assertItems(5)

        selector.update(Three)

        state.assertItems(null, Four, Five, Two, Three)
        idState.assertItems(null, 4, 5, 2, 3)
        updates.assertItems(Five, Three)
        idUpdates.assertItems(5, 3)
    }

    @Test
    fun verifyDeselectIfNotInItems() {
        selector.items.set(values().toList())
        selector.set(Four)

        state.assertItems(null, Four)
        idState.assertItems(null, 4)
        updates.assertItems()
        idUpdates.assertItems()

        selector.items.set(listOf(One, Two, Three))

        state.assertItems(null, Four, null)
        idState.assertItems(null, 4, null)
        updates.assertItems(null)
        idUpdates.assertItems(null)
    }

}