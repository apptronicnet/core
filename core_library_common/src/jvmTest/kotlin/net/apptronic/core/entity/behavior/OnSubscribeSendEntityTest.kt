package net.apptronic.core.entity.behavior

import net.apptronic.core.assertListEquals
import net.apptronic.core.context.component.Component
import net.apptronic.core.entity.commons.typedEvent
import net.apptronic.core.testutils.createTestContext
import org.junit.Test

class OnSubscribeSendEntityTest {

    val component = Component(createTestContext())
    val source = component.typedEvent<String>()
    val results = mutableListOf<String>()

    init {
        source.onSubscribe("OnSubscribe").subscribe {
            results.add(it)
        }
    }

    @Test
    fun verifyOnSubscribe() {
        assertListEquals(results, listOf("OnSubscribe"))

        source.update("One")
        assertListEquals(results, listOf("OnSubscribe", "One"))

        source.update("Two")
        assertListEquals(results, listOf("OnSubscribe", "One", "Two"))

        source.update("Three")
        assertListEquals(results, listOf("OnSubscribe", "One", "Two", "Three"))
    }

}