package net.apptronic.core.component

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.terminate
import net.apptronic.core.component.entity.entities.setAs
import net.apptronic.core.component.extensions.BaseComponent
import net.apptronic.core.component.lifecycle.enterStage
import net.apptronic.core.component.lifecycle.exitStage
import net.apptronic.core.testutils.BaseTestComponent
import net.apptronic.core.testutils.TestLifecycle
import net.apptronic.core.testutils.testContext
import org.junit.Test

class ContextInterpopTest {

    class Parent : BaseTestComponent() {

        val data = value("One")
        val dataReflection = value<String>().setAs(data)

    }

    class Child(context: Context) : BaseComponent(context) {

        val dataReflection = value("Undefined")

    }

    class Another(context: Context) : BaseComponent(context) {

        val dataReflection = value("Undefined")

    }

    private val parent = Parent()
    private val child = Child(testContext(parent = parent.context))
    private val another = Another(testContext())

    @Test
    fun shouldUpdateFromParent() {
        child.dataReflection.setAs(parent.data)

        assert(parent.dataReflection.get() == "One")
        assert(child.dataReflection.get() == "One")

        parent.data.set("Two")
        assert(parent.dataReflection.get() == "Two")
        assert(child.dataReflection.get() == "Two")
    }

    @Test
    fun shouldNotUnsubscribeWhenParentExitFromStage() {

        assert(parent.dataReflection.get() == "One")

        enterStage(parent.context, TestLifecycle.STAGE_CREATED)

        child.dataReflection.setAs(parent.data)

        parent.data.set("Two")
        assert(parent.dataReflection.get() == "Two")
        assert(child.dataReflection.get() == "Two")

        exitStage(parent.context, TestLifecycle.STAGE_CREATED)

        parent.data.set("Three")
        assert(parent.dataReflection.get() == "Three")
        assert(child.dataReflection.get() == "Three")

        enterStage(parent.context, TestLifecycle.STAGE_CREATED)

        parent.data.set("Four")
        assert(parent.dataReflection.get() == "Four")
        assert(child.dataReflection.get() == "Four")
    }

    @Test
    fun shouldUnsubsribeWhenParentTerminated() {

        assert(parent.dataReflection.get() == "One")

        enterStage(parent.context, TestLifecycle.STAGE_CREATED)

        child.dataReflection.setAs(parent.data)

        parent.data.set("Two")
        assert(parent.dataReflection.get() == "Two")
        assert(child.dataReflection.get() == "Two")

        parent.context.lifecycle.terminate()
        assert(parent.getLifecycle().isTerminated())
        assert(child.context.lifecycle.isTerminated())

        parent.data.set("Three")
        assert(parent.dataReflection.get() == "Two")
        assert(child.dataReflection.get() == "Two")

    }

    @Test
    fun shouldUnsubscribeWhenChildExitFromStage() {

        assert(parent.dataReflection.get() == "One")

        enterStage(child.context, TestLifecycle.STAGE_CREATED)

        child.dataReflection.setAs(parent.data)

        parent.data.set("Two")
        assert(parent.dataReflection.get() == "Two")
        assert(child.dataReflection.get() == "Two")

        exitStage(child.context, TestLifecycle.STAGE_CREATED)

        parent.data.set("Three")
        assert(parent.dataReflection.get() == "Three")
        assert(child.dataReflection.get() == "Two")

        enterStage(child.context, TestLifecycle.STAGE_CREATED)

        parent.data.set("Four")
        assert(parent.dataReflection.get() == "Four")
        assert(child.dataReflection.get() == "Two")
    }

    @Test
    fun shouldIgnoreSourceStage() {

        assert(parent.dataReflection.get() == "One")

        enterStage(parent.context, TestLifecycle.STAGE_CREATED)

        another.dataReflection.setAs(parent.data)

        parent.data.set("Two")
        assert(parent.dataReflection.get() == "Two")
        assert(another.dataReflection.get() == "Two")

        exitStage(parent.context, TestLifecycle.STAGE_CREATED)

        parent.data.set("Three")
        assert(parent.dataReflection.get() == "Three")
        assert(another.dataReflection.get() == "Three")

        enterStage(parent.context, TestLifecycle.STAGE_CREATED)

        parent.data.set("Four")
        assert(parent.dataReflection.get() == "Four")
        assert(parent.dataReflection.get() == "Four")
    }

    @Test
    fun shouldUnsubscribeOnExitStage() {

        assert(parent.dataReflection.get() == "One")

        enterStage(another.context, TestLifecycle.STAGE_CREATED)

        another.dataReflection.setAs(parent.data)

        parent.data.set("Two")
        assert(parent.dataReflection.get() == "Two")
        assert(another.dataReflection.get() == "Two")

        exitStage(another.context, TestLifecycle.STAGE_CREATED)

        parent.data.set("Three")
        assert(parent.dataReflection.get() == "Three")
        assert(another.dataReflection.get() == "Two")

        enterStage(another.context, TestLifecycle.STAGE_CREATED)

        parent.data.set("Four")
        assert(parent.dataReflection.get() == "Four")
        assert(another.dataReflection.get() == "Two")
    }

    @Test
    fun shouldUnsubscribeOnSourceTerminated() {

        assert(parent.dataReflection.get() == "One")

        enterStage(parent.context, TestLifecycle.STAGE_CREATED)

        another.dataReflection.setAs(parent.data)

        parent.data.set("Two")
        assert(parent.dataReflection.get() == "Two")
        assert(another.dataReflection.get() == "Two")

        parent.getLifecycle().terminate()

        parent.data.set("Three")
        assert(parent.dataReflection.get() == "Two")
        assert(another.dataReflection.get() == "Two")

    }

    @Test
    fun shouldIgnoreIfSourceTerminated() {

        assert(parent.dataReflection.get() == "One")

        parent.getLifecycle().terminate()

        another.dataReflection.setAs(parent.data)

        parent.data.set("Two")
        assert(parent.dataReflection.get() == "One")
        assert(another.dataReflection.get() == "Undefined")

    }

    @Test
    fun shouldIgnoreIfTargetTerminated() {

        assert(parent.dataReflection.get() == "One")

        another.context.terminate()

        another.dataReflection.setAs(parent.data)

        parent.data.set("Two")
        assert(parent.dataReflection.get() == "Two")
        assert(another.dataReflection.get() == "Undefined")

    }

}