package net.apptronic.core.component.entity.entities

import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.component.extensions.BaseComponent
import net.apptronic.core.testutils.testContext
import org.junit.Test

class LoadPropertyTest {

    private var counter = 1
    private val component = BaseComponent(testContext())

    @Test
    fun shouldCorrectWorkWithGeneric() {
        val loadProperty: LoadProperty<Unit, Int> = component.genericLoadProperty {
            counter++
        }
        assert(loadProperty.isSet())
        assert(loadProperty.get() == 1)
        loadProperty.reload()
        assert(loadProperty.get() == 2)
        loadProperty.subscribe {
            assert(it == 2)
        }
    }

    @Test
    fun shouldReloadOnSubscribe() {
        val loadProperty: LoadProperty<Unit, Int> = component.genericLoadProperty {
            counter++
        }
        loadProperty.reloadOnSubscribe = true
        assert(loadProperty.isSet())
        assert(loadProperty.get() == 1)
        loadProperty.subscribe {
            assert(it == 2)
        }
        assert(loadProperty.get() == 2)
    }

    @Test
    fun shouldWorkLazily() {
        val loadProperty: LoadProperty<Unit, Int> = component.genericLoadProperty(lazy = true) {
            counter++
        }
        assert(loadProperty.isSet().not())
        loadProperty.reload()
        assert(loadProperty.isSet().not())
        var expectedObserver1value = 1
        var expectedIsNotified = true
        val subscription1 = loadProperty.subscribe {
            assert(it == expectedObserver1value)
            assert(expectedIsNotified)
        }
        assert(loadProperty.isSet())
        assert(loadProperty.get() == 1)
        expectedObserver1value = 2
        loadProperty.reload()
        assert(loadProperty.get() == 2)
        expectedIsNotified = false
        subscription1.unsubscribe()
        loadProperty.reload()
        assert(loadProperty.get() == 2)
    }

    @Test
    fun shouldSwitchLaziness() {
        val loadProperty: LoadProperty<Unit, Int> = component.genericLoadProperty(lazy = true) {
            counter++
        }
        assert(loadProperty.isSet().not())
        loadProperty.reload()
        assert(loadProperty.isSet().not())
        loadProperty.setLazy(false)
        assert(loadProperty.get() == 1)
        loadProperty.reload()
        assert(loadProperty.get() == 2)
        loadProperty.setLazy(true)
        loadProperty.reload()
        assert(loadProperty.get() == 2)
    }

}