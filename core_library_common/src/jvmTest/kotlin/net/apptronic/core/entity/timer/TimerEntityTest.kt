package net.apptronic.core.entity.timer

import kotlinx.coroutines.test.TestCoroutineDispatcher
import net.apptronic.core.BaseContextTest
import net.apptronic.core.base.initTestNanoTime
import net.apptronic.core.base.shiftTestTimeMillis
import net.apptronic.core.context.coroutines.CoroutineDispatchers
import net.apptronic.core.record
import net.apptronic.core.test.testContext
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TimerEntityTest : BaseContextTest() {

    init {
        initTestNanoTime()
    }

    private val testDispatcher = TestCoroutineDispatcher()
    override val context = testContext(
        coroutineDispatchers = CoroutineDispatchers(testDispatcher)
    )

    fun advanceTimeBy(timeMs: Long) {
        shiftTestTimeMillis(timeMs)
        testDispatcher.advanceTimeBy(timeMs)
    }

    @Test
    fun verifyRunning() {
        val timer = timerEntity(1000L)
        assertFalse(timer.isRunning)
        val record = timer.record()

        record.assertItems()

        timer.start()
        assertTrue(timer.isRunning)
        record.assertItems(
            TimerTick(0, 0, false)
        )
        record.clear()

        advanceTimeBy(500) // 5000
        record.assertItems()

        advanceTimeBy(500) // 1000
        record.assertItems(
            TimerTick(1, 1000, false)
        )
        record.clear()

        advanceTimeBy(1000) // 2000
        record.assertItems(
            TimerTick(2, 2000, false)
        )
        assertTrue(timer.isRunning)
    }

    @Test
    fun onLimitZeroSingleTick() {
        val timer = timerEntity(1000, limit = 0)
        val record = timer.record()
        timer.start()
        record.assertItems(
            TimerTick(0, 0, true)
        )
        assertFalse(timer.isRunning)
        record.clear()

        advanceTimeBy(1000)
        advanceTimeBy(1000)
        advanceTimeBy(1000)
        advanceTimeBy(1000)
        advanceTimeBy(1000) // 5000
        record.assertItems()
    }

    @Test
    fun finiteLimit() {
        val timer = timerEntity(1000, limit = 2)
        val record = timer.record()
        timer.start()
        assertTrue(timer.isRunning)

        advanceTimeBy(3000L) // 3000
        record.assertItems(
            TimerTick(0, 0, false),
            TimerTick(1, 1000, false),
            TimerTick(2, 2000, true),
        )
        assertFalse(timer.isRunning)
    }

    @Test
    fun changeIntervalDynamically() {
        val timer = timerEntity(1000)
        val record = timer.record()

        timer.start()

        advanceTimeBy(1000L) // 1000
        record.assertItems(
            TimerTick(0, 0, false),
            TimerTick(1, 1000, false),
        )
        record.clear()

        timer.interval = 2000L
        advanceTimeBy(1000L) // 2000
        record.assertItems(
            TimerTick(2, 2000, false),
        )
        record.clear()

        advanceTimeBy(1000L) // 3000
        record.assertItems()

        advanceTimeBy(1000L) // 4000
        record.assertItems(
            TimerTick(3, 4000, false),
        )
        assertTrue(timer.isRunning)
    }

    @Test
    fun changeLimitDynamically() {
        val timer = timerEntity(1000)
        val record = timer.record()

        timer.start()
        record.assertItems(
            TimerTick(0, 0, false),
        )
        record.clear()

        advanceTimeBy(1000)
        advanceTimeBy(1000)
        advanceTimeBy(1000)
        advanceTimeBy(1000)
        record.assertSize(4)
        record.clear()

        timer.limit = 5
        advanceTimeBy(1000)
        record.assertItems(
            TimerTick(5, 5000, true)
        )
        record.clear()
        assertFalse(timer.isRunning)

        advanceTimeBy(1000)
        advanceTimeBy(1000)
        advanceTimeBy(1000)
        record.assertItems()
        assertFalse(timer.isRunning)
    }

    @Test
    fun startWithChangedInterval() {
        val timer = timerEntity(1000)
        val record = timer.record()

        timer.start(2000)
        record.assertItems(
            TimerTick(0, 0, false),
        )
        record.clear()

        advanceTimeBy(1000) // 1000
        record.assertItems()

        advanceTimeBy(500) // 1500
        record.assertItems()

        advanceTimeBy(500) // 2000
        record.assertItems(
            TimerTick(1, 2000, false),
        )
    }

    @Test
    fun intervalIsZero() {
        val timer = timerEntity(0)
        val record = timer.record()

        timer.start()
        record.assertItems(
            TimerTick(0, 0, true),
        )
        record.clear()
        assertFalse(timer.isRunning)

        advanceTimeBy(1000)
        advanceTimeBy(1000)
        advanceTimeBy(1000)
        record.assertItems()
        assertFalse(timer.isRunning)
    }

}