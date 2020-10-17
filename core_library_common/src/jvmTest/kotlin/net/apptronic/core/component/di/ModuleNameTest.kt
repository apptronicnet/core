package net.apptronic.core.component.di

import org.junit.Test
import kotlin.test.assertEquals

class ModuleNameTest {

    @Test
    fun verifyProvidedName() {
        val module = declareModule("Module name") {
            // EMPTY
        }
        assertEquals(module.name, "Module name")
    }

    @Test
    fun verifyGeneratedName() {
        val module = declareModule {
            // EMPTY
        }
        assertEquals(module.name, "ModuleNameTest.kt:18")
    }

}