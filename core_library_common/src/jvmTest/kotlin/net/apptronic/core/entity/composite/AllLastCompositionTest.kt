package net.apptronic.core.entity.composite

import net.apptronic.core.assertListEquals
import net.apptronic.core.context.component.Component
import net.apptronic.core.context.terminate
import net.apptronic.core.entity.typedEvent
import net.apptronic.core.record
import net.apptronic.core.testutils.createTestContext
import org.junit.After
import org.junit.Test

class AllLastCompositionTest : Component(createTestContext()) {

    private val item1 = typedEvent<Int>()
    private val item2 = typedEvent<Int>()
    private val item3 = typedEvent<Int>()

    private val compose = allLastOf(item1, item2, item3)
    private val records = compose.record()

    private fun assertCompose(size: Int, items: List<Int>) {
        records.assertSize(size)
        val last = records.get().last()
        assertListEquals(last, items)
    }

    @Test
    fun verifyLastItem() {
        records.assertItems()

        item1.update(1)
        records.assertItems()

        item1.update(2)
        records.assertItems()

        item2.update(3)
        records.assertItems()

        item3.update(4)
        assertCompose(1, listOf(2, 3, 4))

        item1.update(5)
        assertCompose(2, listOf(5, 3, 4))

        item1.update(6)
        assertCompose(3, listOf(6, 3, 4))

        item3.update(7)
        assertCompose(4, listOf(6, 3, 7))

        item2.update(8)
        assertCompose(5, listOf(6, 8, 7))
    }

    @After
    fun after() {
        terminate()
    }

}