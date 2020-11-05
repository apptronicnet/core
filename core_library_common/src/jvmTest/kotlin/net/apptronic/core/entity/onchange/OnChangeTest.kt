package net.apptronic.core.entity.onchange

import net.apptronic.core.record
import net.apptronic.core.testutils.createTestContext
import kotlin.test.Test

class OnChangeTest {

    val context = createTestContext()

    @Test
    fun verifySendChangesToExistingSubscribers() {
        val changeValue = context.onChangeValue<Int, String>()
        val changes = changeValue.record()
        changeValue.set(1)
        changes.assertItems(
                Next<Int, String>(1, null)
        )
        changeValue.set(2, "1 -> 2")
        changes.assertItems(
                Next<Int, String>(1, null),
                Next<Int, String>(2, "1 -> 2")
        )
        changeValue.set(3, "2 -> 3")
        changes.assertItems(
                Next<Int, String>(1, null),
                Next<Int, String>(2, "1 -> 2"),
                Next<Int, String>(3, "2 -> 3")
        )
        changeValue.set(4)
        changes.assertItems(
                Next<Int, String>(1, null),
                Next<Int, String>(2, "1 -> 2"),
                Next<Int, String>(3, "2 -> 3"),
                Next<Int, String>(4, null)
        )
    }

    @Test
    fun verifyDoNotSendChangesToNewSubscriber() {
        val changeValue = context.onChangeValue<Int, String>()
        changeValue.set(1)
        changeValue.set(2, "1 -> 2")
        val changes = changeValue.record()
        changes.assertItems(
                Next<Int, String>(2, null)
        )
        changeValue.set(3, "2 -> 3")
        changes.assertItems(
                Next<Int, String>(2, null),
                Next<Int, String>(3, "2 -> 3")
        )
    }

    @Test
    fun verifyReflectiveValueWithSetFirstfromValue() {
        val changeValue = context.onChangeValue<Int, String>()
        val changesRecords = changeValue.record()
        val value = changeValue.getValueEntity()
        val valueRecords = value.record()

        value.set(1)
        valueRecords.assertItems(1)
        changesRecords.assertItems(
                Next<Int, String>(1, null)
        )

        value.set(2)
        valueRecords.assertItems(1, 2)
        changesRecords.assertItems(
                Next<Int, String>(1, null),
                Next<Int, String>(2, null)
        )

        changeValue.set(3, "2 -> 3")
        assert(value.get() == 3)
        valueRecords.assertItems(1, 2, 3)
        changesRecords.assertItems(
                Next<Int, String>(1, null),
                Next<Int, String>(2, null),
                Next<Int, String>(3, "2 -> 3")
        )
    }

    @Test
    fun verifyReflectiveValueWithSetFirstfromChangeable() {
        val changeValue = context.onChangeValue<Int, String>()
        val changesRecords = changeValue.record()
        val value = changeValue.getValueEntity()
        val valueRecords = value.record()

        changeValue.set(1)
        valueRecords.assertItems(1)
        changesRecords.assertItems(
                Next<Int, String>(1, null)
        )

        changeValue.set(2, "1 -> 2")
        valueRecords.assertItems(1, 2)
        changesRecords.assertItems(
                Next<Int, String>(1, null),
                Next<Int, String>(2, "1 -> 2")
        )

        value.set(3)
        assert(value.get() == 3)
        valueRecords.assertItems(1, 2, 3)
        changesRecords.assertItems(
                Next<Int, String>(1, null),
                Next<Int, String>(2, "1 -> 2"),
                Next<Int, String>(3, null)
        )
    }

}