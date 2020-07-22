package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.mvvm.viewmodel.ViewModel
import org.junit.Test
import kotlin.test.assertEquals

class ViewModelFactorySameIdsTest {

    object ShortBuilder : ViewModelBuilder<Short, String, ViewModel> {

        override fun getId(item: Short): String {
            return item.toString()
        }

        override fun onCreateViewModel(parent: Context, item: Short): ViewModel {
            return ViewModel(parent.viewModelContext())
        }

    }

    object IntBuilder : ViewModelBuilder<Int, String, ViewModel> {

        override fun getId(item: Int): String {
            return item.toString()
        }

        override fun onCreateViewModel(parent: Context, item: Int): ViewModel {
            return ViewModel(parent.viewModelContext())
        }

    }

    object LongBuilder : ViewModelBuilder<Long, String, ViewModel> {

        override fun getId(item: Long): String {
            return item.toString()
        }

        override fun onCreateViewModel(parent: Context, item: Long): ViewModel {
            return ViewModel(parent.viewModelContext())
        }

    }

    @Test
    fun factoryShouldReturnDifferentIds() {
        val factory = ShortBuilder + IntBuilder + LongBuilder

        val shortId = ShortBuilder.getId(1.toShort())
        val intId = IntBuilder.getId(1.toInt())
        val longId = LongBuilder.getId(1.toLong())
        assertEquals(shortId, intId)
        assertEquals(shortId, longId)
        assertEquals(intId, longId)

        val shortFactoryId1 = factory.getId(1.toShort())
        val shortFactoryId2 = factory.getId(1.toShort())
        assertEquals(shortFactoryId1, shortFactoryId2)

        val intFactoryId1 = factory.getId(1.toInt())
        val intFactoryId2 = factory.getId(1.toInt())
        assertEquals(intFactoryId1, intFactoryId2)

        val longFactoryId1 = factory.getId(1.toLong())
        val longFactoryId2 = factory.getId(1.toLong())
        assertEquals(longFactoryId1, longFactoryId2)

        assertEquals(shortFactoryId1, intFactoryId1)
        assertEquals(shortFactoryId1, longFactoryId1)
        assertEquals(intFactoryId1, longFactoryId1)
    }

}