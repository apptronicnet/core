package net.apptronic.core.context.di

import net.apptronic.core.BaseContextTest
import net.apptronic.core.context.dependencyModule
import net.apptronic.core.test.testContext
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

private val StringInstanceDescriptor = dependencyDescriptor<String>()

class InstanceProviderTest : BaseContextTest() {

    interface InstanceInterface

    object InstanceObject : InstanceInterface

    override val context = testContext {
        dependencyModule {
            instance(StringInstanceDescriptor, "Hello!")
            instance(123)
            instance<InstanceInterface>(InstanceObject)
        }
    }

    @Test
    fun verifyProvidesSimpleType() {
        val instance = dependencyProvider.inject<Int>()
        assertEquals(123, instance)
    }

    @Test
    fun verifyProvidesDescriptorType() {
        val instance = dependencyProvider.inject(StringInstanceDescriptor)
        assertEquals("Hello!", instance)
    }

    @Test
    fun verifyProvidesTyped() {
        val instance = dependencyProvider.inject<InstanceInterface>()
        assertSame(InstanceObject, instance)
    }

}