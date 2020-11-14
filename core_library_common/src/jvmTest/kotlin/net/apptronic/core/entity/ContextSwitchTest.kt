package net.apptronic.core.entity

import net.apptronic.core.context.childContext
import net.apptronic.core.context.lifecycle.Lifecycle
import net.apptronic.core.context.lifecycle.enterStage
import net.apptronic.core.context.lifecycle.exitStage
import net.apptronic.core.context.terminate
import net.apptronic.core.entity.base.Property
import net.apptronic.core.entity.base.Value
import net.apptronic.core.entity.commons.*
import net.apptronic.core.testutils.TEST_LIFECYCLE
import net.apptronic.core.testutils.TestLifecycle
import net.apptronic.core.testutils.createTestContext
import org.junit.Test
import kotlin.test.assertEquals

abstract class ContextSwitchTest {

    val baseContext = createTestContext()

    val sourceContext = baseContext.childContext(lifecycleDefinition = TEST_LIFECYCLE)
    val targetContext = baseContext.childContext(lifecycleDefinition = TEST_LIFECYCLE)

    val source: Value<String> = sourceContext.value<String>()
    abstract fun createResult(): Property<String>

    @Test
    fun shouldSwitchContext() {
        val result = createResult()

        source.set("One")
        assertEquals(result.get(), "One")
    }

    @Test
    fun shouldStopEmittingWhenSourceTerminated() {
        val result = createResult()

        source.set("One")
        assertEquals(result.get(), "One")

        sourceContext.terminate()
        source.set("Two")
        assertEquals(result.get(), "One")
    }

    @Test
    fun shouldStopEmittingWhenTargetTerminated() {
        val result = createResult()

        source.set("One")
        assertEquals(result.get(), "One")

        targetContext.terminate()
        source.set("Two")
        assertEquals(result.get(), "One")
    }

    /**
     * This test verifies that subscription is not depends on lifecycle of [sourceContext] (until it is terminated)
     */
    @Test
    fun shouldIgnoreSourceContextStages() {
        sourceContext.enterStage(TestLifecycle.STAGE_WORKING)
        assertEquals(sourceContext.lifecycle.getActiveStage()!!.getStageDefinition(), TestLifecycle.STAGE_WORKING)

        val result = createResult()
        source.set("One")
        assertEquals(result.get(), "One")

        sourceContext.exitStage(TestLifecycle.STAGE_CREATED)
        assertEquals(sourceContext.lifecycle.getActiveStage()!!.getStageDefinition(), Lifecycle.ROOT_STAGE)

        source.set("Two")
        assertEquals(result.get(), "Two")
    }

    /**
     * This test verifies that subscription is not depends on lifecycle of [sourceContext] (until it is terminated)
     */
    @Test
    fun shouldMatchTargetLifecycleStages() {
        targetContext.enterStage(TestLifecycle.STAGE_WORKING)
        assertEquals(targetContext.lifecycle.getActiveStage()!!.getStageDefinition(), TestLifecycle.STAGE_WORKING)

        val result = createResult()
        source.set("One")
        assertEquals(result.get(), "One")

        targetContext.exitStage(TestLifecycle.STAGE_CREATED)
        assertEquals(targetContext.lifecycle.getActiveStage()!!.getStageDefinition(), Lifecycle.ROOT_STAGE)

        source.set("Two")
        // as result is created on STAGE_WORKING it will be unsubscribed when it is exiteds
        assertEquals(result.get(), "One")
    }

}

class ContextSwitchEntityTest : ContextSwitchTest() {

    override fun createResult(): Property<String> {
        return source.switchContext(targetContext).asProperty()
    }

}

class SubscribeOnContextTest : ContextSwitchTest() {

    override fun createResult(): Property<String> {
        val result = targetContext.value<String>()
        source.subscribe(targetContext) {
            result.set(it)
        }
        return result
    }

}

class SetFromOnContextTest : ContextSwitchTest() {

    override fun createResult(): Property<String> {
        return targetContext.value<String>().setAs(source)
    }

}

class PropertyAsContextTest : ContextSwitchTest() {

    override fun createResult(): BaseProperty<String> {
        return targetContext.property(source)
    }

}

class SetToOnContextTest : ContextSwitchTest() {

    override fun createResult(): Property<String> {
        return targetContext.value<String>().also {
            source.setTo(it)
        }
    }

}