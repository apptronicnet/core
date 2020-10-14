package net.apptronic.core.component.entity.composite

import net.apptronic.core.component.context.terminate
import net.apptronic.core.component.extensions.BaseComponent
import net.apptronic.core.component.typedEvent
import net.apptronic.core.record
import net.apptronic.core.testutils.testContext
import org.junit.After
import org.junit.Test

class LastItemCompositionTest : BaseComponent(testContext()) {

    private val item1 = typedEvent<Int>()
    private val item2 = typedEvent<Int>()
    private val item3 = typedEvent<Int>()

    private val compose = lastItemOf(item1, item2, item3)
    private val records = compose.record()

    @Test
    fun verifyLastItem() {
        records.assertItems()
        item1.update(1)
        item1.update(2)
        item2.update(3)
        item3.update(4)
        item2.update(5)
        item2.update(6)
        item3.update(7)
        item1.update(8)
        records.assertItems(1, 2, 3, 4, 5, 6, 7, 8)
    }

    @After
    fun after() {
        terminate()
    }

}