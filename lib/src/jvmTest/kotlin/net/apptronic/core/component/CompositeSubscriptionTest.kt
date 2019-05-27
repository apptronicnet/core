package net.apptronic.core.component

import net.apptronic.core.base.utils.BaseTestComponent
import net.apptronic.core.base.utils.TestLifecycle
import net.apptronic.core.component.entity.entities.setAs
import net.apptronic.core.component.lifecycle.enterStage
import net.apptronic.core.component.lifecycle.exitStage
import org.junit.Test

class CompositeSubscriptionTest {

    class Parent : BaseTestComponent() {

        val data = value("One")
        val dataReflection = value<String>().setAs(data)

    }

    class Child : BaseTestComponent() {

        val dataReflection = value("Two")

    }

    private val parent = Parent()
    private val child = Child()

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
    fun shouldUnsubscribeWhenParentExits() {

        assert(parent.dataReflection.get() == "One")

        enterStage(parent, TestLifecycle.STAGE_CREATED)

        child.dataReflection.setAs(parent.data)

        parent.data.set("Two")
        assert(parent.dataReflection.get() == "Two")
        assert(child.dataReflection.get() == "Two")

        exitStage(parent, TestLifecycle.STAGE_CREATED)

        parent.data.set("Three")
        assert(parent.dataReflection.get() == "Three")
        assert(child.dataReflection.get() == "Two")

        exitStage(parent, TestLifecycle.STAGE_CREATED)

        parent.data.set("Four")
        assert(parent.dataReflection.get() == "Four")
        assert(child.dataReflection.get() == "Two")
    }

    @Test
    fun shouldUnsubscribeWhenChildExits() {

        assert(parent.dataReflection.get() == "One")

        enterStage(child, TestLifecycle.STAGE_CREATED)

        child.dataReflection.setAs(parent.data)

        parent.data.set("Two")
        assert(parent.dataReflection.get() == "Two")
        assert(child.dataReflection.get() == "Two")

        exitStage(child, TestLifecycle.STAGE_CREATED)

        parent.data.set("Three")
        assert(parent.dataReflection.get() == "Three")
        assert(child.dataReflection.get() == "Two")

        exitStage(child, TestLifecycle.STAGE_CREATED)

        parent.data.set("Four")
        assert(parent.dataReflection.get() == "Four")
        assert(child.dataReflection.get() == "Two")
    }

}