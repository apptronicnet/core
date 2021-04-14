package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.context.Context
import net.apptronic.core.context.childContext
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.ViewModel
import org.junit.Test
import kotlin.test.assertEquals

class ViewModelFactorySameIdsTest {

    object ShortAdapter : ViewModelAdapter<Short, String, IViewModel> {

        override fun getItemId(item: Short): String {
            return item.toString()
        }

        override fun createViewModel(parent: Context, item: Short): IViewModel {
            return ViewModel(parent.childContext())
        }

    }

    object IntAdapter : ViewModelAdapter<Int, String, IViewModel> {

        override fun getItemId(item: Int): String {
            return item.toString()
        }

        override fun createViewModel(parent: Context, item: Int): IViewModel {
            return ViewModel(parent.childContext())
        }

    }

    object LongAdapter : ViewModelAdapter<Long, String, IViewModel> {

        override fun getItemId(item: Long): String {
            return item.toString()
        }

        override fun createViewModel(parent: Context, item: Long): IViewModel {
            return ViewModel(parent.childContext())
        }

    }

    @Test
    fun factoryShouldReturnDifferentIds() {
        val factory = ShortAdapter + IntAdapter + LongAdapter

        val shortId = ShortAdapter.getItemId(1.toShort())
        val intId = IntAdapter.getItemId(1.toInt())
        val longId = LongAdapter.getItemId(1.toLong())
        assertEquals(shortId, intId)
        assertEquals(shortId, longId)
        assertEquals(intId, longId)

        val shortFactoryId1 = factory.getItemId(1.toShort())
        val shortFactoryId2 = factory.getItemId(1.toShort())
        assertEquals(shortFactoryId1, shortFactoryId2)

        val intFactoryId1 = factory.getItemId(1.toInt())
        val intFactoryId2 = factory.getItemId(1.toInt())
        assertEquals(intFactoryId1, intFactoryId2)

        val longFactoryId1 = factory.getItemId(1.toLong())
        val longFactoryId2 = factory.getItemId(1.toLong())
        assertEquals(longFactoryId1, longFactoryId2)

        assertEquals(shortFactoryId1, intFactoryId1)
        assertEquals(shortFactoryId1, longFactoryId1)
        assertEquals(intFactoryId1, longFactoryId1)
    }

}