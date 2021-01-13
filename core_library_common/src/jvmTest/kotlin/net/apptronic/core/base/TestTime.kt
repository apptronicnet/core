package net.apptronic.core.base

internal var testNanoTime = 0L

internal class TestNanoTimeProvider : NanoTimeProvider {

    override fun nanoTime(): Long {
        return testNanoTime
    }

}

fun initTestNanoTime() {
    testNanoTime = 0L
    val cls = Class.forName("net.apptronic.core.base.TimeKt")
    val field = cls.getDeclaredField("nanoTimeProvider")
    field.isAccessible = true
    field.set(null, TestNanoTimeProvider())
}

fun shiftTestTimeMillis(shiftMs: Long) {
    testNanoTime += (shiftMs * 1_000_000)
}