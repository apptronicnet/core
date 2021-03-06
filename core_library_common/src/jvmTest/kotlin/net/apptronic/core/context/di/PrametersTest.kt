package net.apptronic.core.context.di

import net.apptronic.core.context.Context
import net.apptronic.core.context.childContext
import net.apptronic.core.context.component.Component
import net.apptronic.core.context.dependencyModule
import net.apptronic.core.context.terminate
import net.apptronic.core.testutils.createTestContext
import org.junit.After
import org.junit.Test
import kotlin.test.assertEquals

val UserIdDescriptor = dependencyDescriptor<String>()

class PrametersTest {

    class UserRepository(val userId: String)

    val Module = declareModule {
        factory {
            UserRepository(
                    provided(UserIdDescriptor)
            )
        }
    }

    val context = createTestContext {
        dependencyModule(Module)
    }

    class UserClientComponent(context: Context, val userId: String) : Component(context) {

        val repository = inject<UserRepository>(parameters {
            add(UserIdDescriptor, userId)
        })

    }

    @Test
    fun verifyParametrizedInject() {
        val component1 = UserClientComponent(context.childContext(), "1")
        val component2 = UserClientComponent(context.childContext(), "2")
        assertEquals(component1.repository.userId, "1")
        assertEquals(component2.repository.userId, "2")
    }

    @After
    fun after() {
        context.terminate()
    }

}