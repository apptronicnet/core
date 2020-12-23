package net.apptronic.core.commons.eventbus

import net.apptronic.core.BaseContextTest
import net.apptronic.core.context.Context
import net.apptronic.core.context.childContext
import net.apptronic.core.context.component.Component
import net.apptronic.core.context.dependencyModule
import net.apptronic.core.context.terminate
import net.apptronic.core.record
import org.junit.Test

private val StringChannel = eventChannel<String>()
private val IntChannel = eventChannel<Int>()

class EventBusTest : BaseContextTest() {

    init {
        context.dependencyModule {
            eventBus()
        }
    }

    class ClientComponent(context: Context) : Component(context) {

        val genericClient = eventBusClient()
        val stringClient = eventBusClient(StringChannel)
        val intClient = eventBusClient(IntChannel)

        val genericRecords = genericClient.record()
        val stringRecords = stringClient.record()
        val intRecords = intClient.record()

        fun clearRecords() {
            genericRecords.clear()
            stringRecords.clear()
            intRecords.clear()
        }

    }

    val client1 = ClientComponent(childContext())
    val client2 = ClientComponent(childContext())
    val client3 = ClientComponent(childContext())

    fun clearRecords() {
        client1.clearRecords()
        client2.clearRecords()
        client3.clearRecords()
    }

    @Test
    fun verifyEventBus() {

        client1.genericClient.postEvent(1)

        client1.genericRecords.assertItems(1)
        client2.genericRecords.assertItems(1)
        client3.genericRecords.assertItems(1)
        client1.intRecords.assertItems()
        client2.intRecords.assertItems()
        client3.intRecords.assertItems()

        clearRecords()

        client1.intClient.postEvent(234)
        client1.genericRecords.assertItems()
        client2.genericRecords.assertItems()
        client3.genericRecords.assertItems()
        client1.intRecords.assertItems(234)
        client2.intRecords.assertItems(234)
        client3.intRecords.assertItems(234)

        clearRecords()

        val helloEvents = listOf("Hello", "Hello", "Hello2", "Hello3", "Hello")
        client1.stringClient.postEvent(helloEvents[0])
        client1.stringClient.postEvent(helloEvents[1])
        client1.stringClient.postEvent(helloEvents[2])
        client2.stringClient.postEvent(helloEvents[3])
        client3.stringClient.postEvent(helloEvents[4])

        client1.genericRecords.assertItems()
        client2.genericRecords.assertItems()
        client3.genericRecords.assertItems()
        client1.stringRecords.assertItems(helloEvents)
        client2.stringRecords.assertItems(helloEvents)
        client3.stringRecords.assertItems(helloEvents)

        clearRecords()

        client3.terminate()
        client3.stringClient.postEvent("123")
        client1.stringRecords.assertItems()
        client2.stringRecords.assertItems()
        client3.stringRecords.assertItems()

        client1.stringClient.postEvent("Tom")
        client1.stringRecords.assertItems("Tom")
        client2.stringRecords.assertItems("Tom")
        client3.stringRecords.assertItems()
    }

}