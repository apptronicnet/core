package net.apptronic.core.context.di

import junit.framework.Assert.fail
import net.apptronic.core.BaseContextTest
import net.apptronic.core.context.dependencyModule
import org.junit.Test
import kotlin.test.assertEquals

class SharedScopeFallbackTimeAndCounterRequiredTest : BaseContextTest() {

    @Test
    fun canCreateZeroFallbacks() {
        context.dependencyModule {
            shared(fallbackCount = 0, fallbackLifetime = 0) {
                Unit
            }
        }
    }

    @Test
    fun cannotCreateFallbackTimeWithCounter() {
        try {
            context.dependencyModule {
                shared(fallbackCount = 0, fallbackLifetime = 1) {
                    Unit
                }
            }
            fail("Bot time and count should be set")
        } catch (e: IllegalArgumentException) {
            assertEquals(
                "Both [fallbackCount] and [fallbackLifetime] should be set to be larger than 0 at same time",
                e.message
            )
        }
    }

    @Test
    fun cannotCreateFallbackCounterWithTime() {
        try {
            context.dependencyModule {
                shared(fallbackCount = 1, fallbackLifetime = 0) {
                    Unit
                }
            }
            fail("Bot time and count should be set")
        } catch (e: IllegalArgumentException) {
            assertEquals(
                "Both [fallbackCount] and [fallbackLifetime] should be set to be larger than 0 at same time",
                e.message
            )
        }
    }

}