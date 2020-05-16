package net.apptronic.core.base.core.di

import net.apptronic.core.component.di.createNullableDescriptor
import net.apptronic.core.testutils.TestContext
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
        context.dependencyDispatcher.addNullableInstance(NullableStringDescriptor, null)
        context.dependencyDispatcher.addNullableInstance(NullableIntDescriptor, null)
    }

    @Test
    fun shouldInjectNulls() {
        val nullString = context.dependencyDispatcher.inject(NullableStringDescriptor)
        val nullInt = context.dependencyDispatcher.inject(NullableIntDescriptor)
        assertNull(nullString)
        assertNull(nullInt)
    }

}