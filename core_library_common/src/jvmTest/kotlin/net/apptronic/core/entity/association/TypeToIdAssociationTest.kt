package net.apptronic.core.entity.association

import net.apptronic.core.BaseContextTest
import net.apptronic.core.entity.commons.value
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

data class SomeTypeWithId(
    val id: Int,
    val name: String
)

private val ONE = SomeTypeWithId(1, "One")
private val TWO = SomeTypeWithId(2, "Two")
private val THREE = SomeTypeWithId(3, "Three")
private val FOUR = SomeTypeWithId(4, "Four")

class TypeToIdAssociationTest : BaseContextTest() {

    private val items = listOf(ONE, TWO, THREE, FOUR)

    private val item = value<SomeTypeWithId?>()
    private val id = item.associate(TypeToIdAssociation(items) { it.id })

    @Test
    fun setIdChangesItem() {
        id.set(null)
        assertNull(item.get())
        id.set(1)
        assertEquals(item.get(), ONE)
        id.set(2)
        assertEquals(item.get(), TWO)
        id.set(3)
        assertEquals(item.get(), THREE)
        id.set(4)
        assertEquals(item.get(), FOUR)
        id.set(5)
        assertNull(item.get())
    }

    @Test
    fun setItemChangesId() {
        item.set(null)
        assertNull(id.get())
        item.set(ONE)
        assertEquals(id.get(), 1)
        item.set(TWO)
        assertEquals(id.get(), 2)
        item.set(THREE)
        assertEquals(id.get(), 3)
        item.set(FOUR)
        assertEquals(id.get(), 4)
        item.set(SomeTypeWithId(5, "Unknown"))
        assertEquals(id.get(), 5)
    }

}