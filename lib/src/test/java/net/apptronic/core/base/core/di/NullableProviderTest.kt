package net.apptronic.core.base.core.di

import net.apptronic.core.base.utils.TestContext
import net.apptronic.core.component.di.createNullableDescriptor
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNull

class NullableProviderTest {

    companion object {
        val NullableStringDescriptor = createNullableDescriptor<String>()
        val NullableIntDescriptor = createNullableDescriptor<Int>()
    }

    val context = TestContext()

    @Before
    fun before() {
        context.getProvider().addNullableInstance(NullableStringDescriptor, null)
        context.getProvider().addNullableInstance(NullableIntDescriptor, null)
    }

    @Test
    fun shouldInjectNulls() {
        val nullString = context.getProvider().inject(NullableStringDescriptor)
        val nullInt = context.getProvider().inject(NullableIntDescriptor)
        assertNull(nullString)
        assertNull(nullInt)
    }

}