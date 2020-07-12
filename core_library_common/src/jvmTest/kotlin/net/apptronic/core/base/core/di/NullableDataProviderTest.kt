package net.apptronic.core.base.core.di

import net.apptronic.core.component.di.dependencyDescriptorNullable
import net.apptronic.core.testutils.testContext
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNull

class NullableDataProviderTest {

    companion object {
        val NullableStringDescriptor = dependencyDescriptorNullable<String>()
        val NullableIntDescriptor = dependencyDescriptorNullable<Int>()
    }

    val context = testContext()

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

    @Test
    fun traceTest() {
        context.dependencyDispatcher.traceDependencyTree().print()
    }

}